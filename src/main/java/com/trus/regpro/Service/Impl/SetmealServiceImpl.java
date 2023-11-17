package com.trus.regpro.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trus.regpro.Common.CustomException;
import com.trus.regpro.DTO.SetmealDTO;
import com.trus.regpro.Entity.Setmeal;
import com.trus.regpro.Entity.SetmealDish;
import com.trus.regpro.Mapper.SetmealMapper;
import com.trus.regpro.Service.SetmealDishService;
import com.trus.regpro.Service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
        implements SetmealService {

        @Autowired
        SetmealDishService setmealDishService;

        @Override
        @Transactional
        public void saveWithDish(SetmealDTO setmealDTO) {
            this.save(setmealDTO);

            List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();

            setmealDishes.stream().map((item)->{
                item.setDishId(setmealDTO.getId());
                return item;
            }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }

    @Transactional
    @Override
    public void removeWithDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(queryWrapper);

        if(count>0){
            throw new CustomException("套餐起售状态，拒绝删除");
        }

        this.removeByIds(ids);


        LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(queryWrapper1);
    }
}
