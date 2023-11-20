package com.trus.regpro.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trus.regpro.Common.R;
import com.trus.regpro.DTO.SetmealDTO;
import com.trus.regpro.Entity.Category;
import com.trus.regpro.Entity.Setmeal;
import com.trus.regpro.Service.CategoryService;
import com.trus.regpro.Service.SetmealDishService;
import com.trus.regpro.Service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    SetmealDishService setmealDishService;

    @Autowired
    SetmealService setmealService;
    
    @Autowired
    CategoryService categoryService;

//    @RequestMapping(value = "/page",method = RequestMethod.GET)
//    R<Page> page(int page, int pageSize){
//        Page setmeals = new Page(page,pageSize);
//
//        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper();
//
////        queryWrapper.like();
//
//
//
//
////        return R.success(page());
//    return null;
//    }

    @Cacheable(value = "setmealCache")
    @RequestMapping(method = RequestMethod.POST)
    public R<String> save(@RequestBody SetmealDTO setmealDTO){
        log.info("套餐信息：{}",setmealDTO);

        setmealService.save(setmealDTO);

        return null;
    }


    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public R<Page> page(int page, int pageSize, String name){

        Page pageInfo = new Page(page,pageSize);

        Page dtoPage = new Page(page,pageSize);
        BeanUtils.copyProperties(pageInfo, dtoPage);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.like(name != null,Setmeal::getName,name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo, queryWrapper);

        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDTO> collect = records.stream().map((item) -> {
            SetmealDTO setmealDTO = new SetmealDTO();
            BeanUtils.copyProperties(item, setmealDTO);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            setmealDTO.setCategoryName(categoryName);
            return setmealDTO;
        }).collect(Collectors.toList());

        dtoPage.setRecords(collect);

        return R.success(dtoPage);
    }

    @CacheEvict(value = "setmealCache", allEntries = true)
    @RequestMapping(method = RequestMethod.DELETE)
    public R<String> delete(@RequestParam List<Long> ids){
            setmealService.removeWithDish(ids);

    return R.success("删除成功");

    }

    @Cacheable(value = "setmealCache", key = "#setmeal.id+'_'+#setmeal.status")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper =new LambdaQueryWrapper();
        queryWrapper.eq(Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }

}

