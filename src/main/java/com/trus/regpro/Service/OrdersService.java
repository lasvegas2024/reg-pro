package com.trus.regpro.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trus.regpro.Entity.Orders;


public interface OrdersService extends IService<Orders> {
    void submit(Orders orders);
}
