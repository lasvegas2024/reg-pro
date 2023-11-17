package com.trus.regpro.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trus.regpro.DTO.DishDTO;
import com.trus.regpro.Entity.Dish;
import com.trus.regpro.Entity.DishFlavor;
import com.trus.regpro.Mapper.DishMapper;
import com.trus.regpro.Service.DishFlavorService;
import com.trus.regpro.Service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
implements DishService {

    @Autowired
    DishFlavorService dishFlavorService;


    @Transactional
    public void saveWithFlover(DishDTO dishDTO){

        this.save(dishDTO);

        Long id = dishDTO.getId();
        List<DishFlavor> dishFlavors = dishDTO.getFlavors();

        dishFlavors = dishFlavors.stream().map((item)->{
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());


        dishFlavorService.saveBatch(dishFlavors);

    }

    @Override
    public DishDTO getByIdWithFlavor(Long id) {
        // 查询菜品基本信息，从dish表查询
        Dish dish = this.getById(id);
        DishDTO dishDto = new DishDTO();
        BeanUtils.copyProperties(dish,dishDto);
        // 查询对应的口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(list);
        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlover(DishDTO dishDTO) {
        this.updateById(dishDTO);


        LambdaQueryWrapper<DishFlavor> queryWrapper =new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId,dishDTO.getId());
        dishFlavorService.remove(queryWrapper);

        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors = flavors.stream().map((item)->{
            item.setDishId(dishDTO.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);



    }





}
