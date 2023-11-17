package com.trus.regpro.Controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.trus.regpro.Common.BaseContext;
import com.trus.regpro.Common.R;
import com.trus.regpro.Entity.AddressBook;
import com.trus.regpro.Service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController {

    @Autowired
    AddressBookService addressBookService;


    @RequestMapping(method = RequestMethod.POST)
    R<AddressBook> save(@RequestBody AddressBook addressBook){
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }


    @RequestMapping(value = "/default",method = RequestMethod.PUT)
    R<AddressBook> setDefault(@RequestBody AddressBook addressBook){
        Long userId = addressBook.getUserId();
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper();
        updateWrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        updateWrapper.set(AddressBook::getIsDefault,0);
        addressBookService.update(updateWrapper);
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }


    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    R<AddressBook> get(@PathVariable Long id){
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook == null) {
            return R.error("没有找到该地址");
        }else{
            return R.success(addressBook);
        }
    }


    @RequestMapping(value = "/default",method = RequestMethod.GET)
    public R<AddressBook> getDefault(){
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault,1);
        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        if (addressBook == null) {
            return R.error("没有找到该地址");
        }else{
            return R.success(addressBook);
        }
    }


    @RequestMapping(value = "/list",method = RequestMethod.GET)
    R<List<AddressBook>> list(AddressBook addressBook){
        addressBook.setUserId(BaseContext.getCurrentId());
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(addressBook.getUserId()!=null,AddressBook::getUserId,addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);
        return R.success(addressBookService.list(queryWrapper));
    }


    @RequestMapping(method = RequestMethod.DELETE)
    public R<String> delete(@RequestParam("ids") Long id){
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getId,id);
        queryWrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        addressBookService.remove(queryWrapper);
        return R.success("删除成功");
    }


    @RequestMapping(method = RequestMethod.PUT)
    R<String> update(@RequestBody AddressBook addressBook){
        if (addressBook == null) {
            return R.error("请求地址异常");
        }
        addressBookService.updateById(addressBook);
        return R.success("修改成功");
    }
}