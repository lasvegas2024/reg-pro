package com.trus.regpro.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trus.regpro.Entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
