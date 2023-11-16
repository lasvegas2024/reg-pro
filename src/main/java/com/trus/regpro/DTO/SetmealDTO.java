package com.trus.regpro.DTO;



import com.trus.regpro.Entity.Setmeal;
import com.trus.regpro.Entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDTO extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
