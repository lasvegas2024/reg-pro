package com.trus.regpro.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trus.regpro.DTO.SetmealDTO;
import com.trus.regpro.Entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    void saveWithDish(SetmealDTO setmealDTO);

    void removeWithDish(List<Long> ids);
}
