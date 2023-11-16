package com.trus.regpro.Controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trus.regpro.Common.BaseContext;
import com.trus.regpro.Common.CommonsConst;
import com.trus.regpro.Common.R;
import com.trus.regpro.Entity.Employee;
import com.trus.regpro.Service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {


    @Autowired
    EmployeeService employeeService =null;


    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){

        String username = employee.getUsername();
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
        LambdaQueryWrapper<Employee> queryWrapper =new LambdaQueryWrapper();
        queryWrapper.eq(Employee::getUsername,username);
        Employee emp = employeeService.getOne(queryWrapper);
        if(emp == null) {
            return R.error(CommonsConst.LOGIN_FAIL);
        }
        if(!emp.getPassword().equals(password)){
            return R.error(CommonsConst.LOGIN_FAIL);
        }
        if(emp.getStatus().equals(CommonsConst.EMPLOYEE_STATUS_NO))
            return R.error(CommonsConst.LOGIN_ACCOUNT_STOP);
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(employee);
    }

    @GetMapping("{id}")
    public R<Employee> getById(@PathVariable String id){
        Employee emp = employeeService.getById(id);
        if (emp!=null) {
        return R.success(emp);
        }
        return R.error("无Id");
    }

    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee){
        BaseContext.setCurrentId((Long)request.getSession().getAttribute("employee"));
        employeeService.updateById(employee);
        return R.success("修改成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){

        log.info("page:{}, pageSize:{}, name:{}",page, pageSize,name);
        Page pageInfo = new Page(page, pageSize);

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name).or()
                .like(StringUtils.isNotEmpty(name),Employee::getUsername,name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        employeeService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }


    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.removeAttribute("employee");
        return R.success("已退出");
    }

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee){


//        LambdaQueryWrapper queryWrapper = new LambdaQueryWrapper();
//        queryWrapper.setEntity(employee);


        employeeService.save(employee);
    return R.success("添加成功");
    }





}
