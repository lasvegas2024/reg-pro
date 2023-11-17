package com.trus.regpro.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trus.regpro.Common.CustomException;
import com.trus.regpro.Entity.Dish;
import com.trus.regpro.Entity.Setmeal;
import com.trus.regpro.Mapper.CategoryMapper;
import com.trus.regpro.Entity.Category;
import com.trus.regpro.Service.CategoryService;
import com.trus.regpro.Service.DishService;
import com.trus.regpro.Service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
        implements CategoryService {

    @Autowired
    DishService dishService;

    @Autowired
    SetmealService setmealService;

    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper();
        dishQueryWrapper.eq(Dish::getId,id);
        int dishCount = dishService.count(dishQueryWrapper);
        if (dishCount > 0){
            throw new CustomException("当前分类已关联菜品，不能删除");
        }

        LambdaQueryWrapper<Setmeal> setmealQueryWrapper = new LambdaQueryWrapper();
        setmealQueryWrapper.eq(Setmeal::getId, id);
        int setmealCount = setmealService.count(setmealQueryWrapper);

        if (setmealCount > 0){

            throw new CustomException("当前分类已关联套餐，不能删除");
        }
        super.removeById(id);

    }
}
