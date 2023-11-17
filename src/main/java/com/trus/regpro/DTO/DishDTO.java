package com.trus.regpro.DTO;

import com.trus.regpro.Entity.Dish;
import com.trus.regpro.Entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class DishDTO extends Dish {

    List<DishFlavor> flavors = new ArrayList<DishFlavor>();


    private String categoryName;

    private Integer copies;
}
