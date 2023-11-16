package com.trus.regpro.Controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trus.regpro.Common.BaseContext;
import com.trus.regpro.Common.R;
import com.trus.regpro.Entity.ShoppingCart;
import com.trus.regpro.Service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    ShoppingCartService shoppingCartService;

    /**
     * 查询购物车
     * @return 购物车集合
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    R<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){

        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);

        Long dishId = shoppingCart.getDishId();
        if (dishId != null) {
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart shoppingCartOne = shoppingCartService.getOne(queryWrapper);

        if (shoppingCartOne != null) {
            shoppingCartOne.setNumber(shoppingCartOne.getNumber()+1);
            shoppingCartService.updateById(shoppingCartOne);
        }else {
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            shoppingCartOne = shoppingCart;
        }
        return R.success(shoppingCart);

    }

    @RequestMapping(value = "/sub",method = RequestMethod.POST)
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){

        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper();
        if (dishId != null) {
            queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
            ShoppingCart one = shoppingCartService.getOne(queryWrapper);
            one.setNumber(one.getNumber()-1);
            Integer number = one.getNumber();

            if(number > 0){
                shoppingCartService.updateById(one);
            }else if(number == 0){
                shoppingCartService.removeById(one.getId());
            }else if (number <0){
                return R.error("操作异常");
            }
            return R.success(one);
        }
        Long setmealId = shoppingCart.getSetmealId();
        if(setmealId!= null){
            queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId())
                    .eq(ShoppingCart::getSetmealId,setmealId);
            ShoppingCart one = shoppingCartService.getOne(queryWrapper);

            one.setNumber(one.getNumber()-1);
            Integer number = one.getNumber();
            if(number >0){
                shoppingCartService.updateById(one);
            }else if (number == 0)
            {
                shoppingCartService.removeById(one);
            }else if (number<0){
                return R.error("操作异常");
            }
            return R.success(one);
        }
        return R.error("操作异常，找不到id");
    }

    @RequestMapping(value = "/clean", method = RequestMethod.DELETE)
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("清空购物车");
    }


}
