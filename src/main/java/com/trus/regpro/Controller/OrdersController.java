package com.trus.regpro.Controller;


import com.trus.regpro.Common.R;
import com.trus.regpro.Entity.Orders;
import com.trus.regpro.Service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/order")
@RestController
@Slf4j
public class OrdersController {

    @Autowired
    OrdersService ordersService;


    @RequestMapping(value = "/submit",method = RequestMethod.POST)
    public R<String> submit(@RequestBody Orders orders){
        ordersService.submit(orders);
        return R.success("下单成功");
    }


}
