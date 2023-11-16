package com.trus.regpro.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trus.regpro.Entity.OrderDetail;
import com.trus.regpro.Mapper.OrderDetailMapper;
import com.trus.regpro.Service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
implements OrderDetailService {
}
