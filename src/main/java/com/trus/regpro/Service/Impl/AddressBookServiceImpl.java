package com.trus.regpro.Service.Impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trus.regpro.Entity.AddressBook;
import com.trus.regpro.Mapper.AddressBookMapper;
import com.trus.regpro.Service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
implements AddressBookService {
}
