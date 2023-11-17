package com.trus.regpro.Controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trus.regpro.Common.BaseContext;
import com.trus.regpro.Common.R;
import com.trus.regpro.Entity.Category;
import com.trus.regpro.Service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService = null;

    @PostMapping
    public R<String> addCategory(HttpServletRequest request, @RequestBody Category category){

        BaseContext.setCurrentId((Long) request.getSession().getAttribute("employee"));
        categoryService.save(category);
        return R.success("添加成功");
    }

    @GetMapping("/page")
    public R<Page> page(HttpServletRequest request, int page, int pageSize){

//        Page<Category> pageItem = new Page<>();
//        pageItem.setPages(page).setSize(pageSize);

        Page<Category> pageItem = new Page<>(page,pageSize);

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();


        categoryService.page(pageItem, queryWrapper);

        return R.success(pageItem);
    }

    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Category category){

        BaseContext.setCurrentId((Long)request.getSession().getAttribute("employee"));
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public R<String> remove(Long ids){
        System.out.println("ids：" + ids);
        categoryService.remove(ids);
        return R.success("已删除");
    }

//    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @GetMapping("/list")
    public R<List<Category>> list(Category category){

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(category.getType()!=null, Category::getType,category.getType());

        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);

        return R.success(list);
    }




}
