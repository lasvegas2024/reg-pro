package com.trus.regpro.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trus.regpro.Mapper.EmployeeMapper;
import com.trus.regpro.Entity.Employee;
import com.trus.regpro.Service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
        implements EmployeeService {
}
