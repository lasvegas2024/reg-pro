package com.trus.regpro.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trus.regpro.DTO.DishDTO;
import com.trus.regpro.Entity.Dish;

public interface DishService extends IService<Dish> {

    void saveWithFlover(DishDTO dishDTO);
    DishDTO getByIdWithFlavor(Long id);

    void updateWithFlover(DishDTO dishDTO);
}
