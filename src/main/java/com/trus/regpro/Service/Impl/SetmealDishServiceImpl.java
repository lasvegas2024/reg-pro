package com.trus.regpro.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trus.regpro.Entity.SetmealDish;
import com.trus.regpro.Mapper.SetmealDishMapper;
import com.trus.regpro.Service.SetmealDishService;
import org.springframework.stereotype.Service;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish>
        implements SetmealDishService {
}
