package com.trus.regpro.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trus.regpro.Entity.ShoppingCart;
import com.trus.regpro.Mapper.ShoppingCartMapper;
import com.trus.regpro.Service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
implements ShoppingCartService {
}
