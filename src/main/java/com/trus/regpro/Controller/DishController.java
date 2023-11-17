package com.trus.regpro.Controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trus.regpro.Common.R;
import com.trus.regpro.DTO.DishDTO;
import com.trus.regpro.Entity.Category;
import com.trus.regpro.Entity.Dish;
import com.trus.regpro.Entity.DishFlavor;
import com.trus.regpro.Service.CategoryService;
import com.trus.regpro.Service.DishFlavorService;
import com.trus.regpro.Service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {


    @Autowired
    private final DishService dishService = null;

    @Autowired
    CategoryService categoryService = null;

    @Autowired
    DishFlavorService dishFlavorService = null;

    @Autowired
    RedisTemplate redisTemplate;


//    @GetMapping("/page")
//    public R<Page> page(int page, int pageSize,String name){
//        log.info("page={},pageSize={},name={}", page, pageSize, name);
//        // 构造分页构造器
//        Page<Dish> pageInfo = new Page(page, pageSize);
//        // 因为前端需要展示分类的名称，所以封装成DishDto对象
//        Page<DishDTO> dishDtoPage = new Page(page, pageSize);
//        // 构造条件
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
//        queryWrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name);
//        // 添加排序
//        queryWrapper.orderByDesc(Dish::getUpdateTime);
//        // 执行查询
//        dishService.page(pageInfo, queryWrapper);
//        // 进行对象拷贝,去除之前已经查出来的集合
//        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
//
//        List<Dish> records = pageInfo.getRecords();
//        List<DishDTO> list = records.stream().map((item)->{
//            DishDTO dishDto = new DishDTO();
//            BeanUtils.copyProperties(item,dishDto);
//            // 获取categoryId
//            Long categoryId = item.getCategoryId();
//            // 给categoryName赋值
//            Category category = categoryService.getById(categoryId);
//            if(category!=null){
//                dishDto.setCategoryName(category.getName());
//            }
//            // 当前菜品ID
//            Long dishId = item.getId();
//            LambdaQueryWrapper<DishFlavor> dishFlavorQueryWrapper= new LambdaQueryWrapper<>();
//            dishFlavorQueryWrapper.eq(DishFlavor::getDishId,dishId);
//            List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorQueryWrapper);
//            dishDto.setFlavors(dishFlavorList);
//            return dishDto;
//        }).collect(Collectors.toList());
//        dishDtoPage.setRecords(list);
//        return R.success(dishDtoPage);
//    }


    @RequestMapping(value = "/page",method = RequestMethod.GET)
//@GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){

        Page<Dish> pageInfo = new Page(page,pageSize);
//        pageInfo.setPages(page);
//        pageInfo.setSize(pageSize);

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.isNotEmpty(name),Dish::getName,name);

        queryWrapper.orderByDesc(Dish::getUpdateTime);

        Page<DishDTO> DishDTOPage = new Page(page,pageSize);

        dishService.page(pageInfo,queryWrapper);

        BeanUtils.copyProperties(pageInfo,DishDTOPage,"records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDTO> collect = records.stream().map((item) -> {
            DishDTO dishDTO = new DishDTO();
            BeanUtils.copyProperties(item, dishDTO);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                dishDTO.setCategoryName(category.getName());
            }
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> list = dishFlavorService.list(dishFlavorLambdaQueryWrapper);

            dishDTO.setFlavors(list);

            return dishDTO;
        }).collect(Collectors.toList());
        DishDTOPage.setRecords(collect);



        return R.success(DishDTOPage);

    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R<DishDTO> getOne(@PathVariable Long id){
        DishDTO dish = dishService.getByIdWithFlavor(id);
        return R.success(dish);
    }

//    @RequestMapping(value = "/status/0",method = RequestMethod.POST)
//    public R<String> onOrClose(Dish dish){
//
//    }



    @RequestMapping(method = RequestMethod.POST)
    public R<String> save(@RequestBody DishDTO dishDTO){

        dishService.saveWithFlover(dishDTO);
        return R.success("添加成功");
    }

    @RequestMapping(method = RequestMethod.PUT)
    public R<String> update(@RequestBody DishDTO dishDTO){

        String key = "dish_"+dishDTO.getCategoryId()+"_"+dishDTO.getStatus();
        redisTemplate.delete(key);

        dishService.updateWithFlover(dishDTO);

        return R.success("修改成功");
    }

//    @RequestMapping(value = "/list",method = RequestMethod.GET)
//    R<List<Dish>> list(Dish dish){
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
//        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        queryWrapper.eq(Dish::getStatus,1);
//        List<Dish> list = dishService.list(queryWrapper);
//        return R.success(list);
//    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    R<List<DishDTO>> list(Dish dish){

        String key = "dish_"+dish.getCategoryId()+"_"+dish.getStatus();

        List<DishDTO> o = (List<DishDTO>)redisTemplate.opsForValue().get(key);

        if(o!=null){
            return R.success(o);
        }
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        queryWrapper.eq(Dish::getStatus,1);
        List<Dish> list = dishService.list(queryWrapper);

        List<DishDTO> dishDTOList = list.stream().map((item)->{
            DishDTO dishDTO = new DishDTO();
            BeanUtils.copyProperties(item,dishDTO);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                dishDTO.setCategoryName(category.getName());
            }
            Long id = item.getId();
            LambdaQueryWrapper<DishFlavor> queryWrapper1 = new LambdaQueryWrapper();
            queryWrapper1.eq(DishFlavor::getDishId,id);
            List<DishFlavor> list1 = dishFlavorService.list(queryWrapper1);
            dishDTO.setFlavors(list1);
            return dishDTO;
        }).collect(Collectors.toList());

        redisTemplate.opsForValue().set(key,dishDTOList,2, TimeUnit.HOURS);

        return R.success(dishDTOList);
    }

    @RequestMapping(value = "/status/{status}",method = RequestMethod.POST)
    public R<String> onOrClose(@PathVariable Integer status, Long[] ids){
        for (int i = 0; i < ids.length; i++) {
            Dish dish= dishService.getById(ids[i]);
            dish.setStatus(status);
            dishService.updateById(dish);
        }
        return R.success("修改成功");
    }
}
