package com.trus.regpro.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trus.regpro.Entity.User;
import com.trus.regpro.Mapper.UserMapper;
import com.trus.regpro.Service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
implements UserService{
}
