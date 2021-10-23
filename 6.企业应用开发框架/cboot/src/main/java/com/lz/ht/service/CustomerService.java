package com.lz.ht.service;

import com.lz.ht.model.Customer;
import com.lz.ht.page.PageModel;
import java.util.List;
import java.util.HashMap;

public interface CustomerService {

    void add(Customer record);

    void updateById(Customer record);

    int deleteById(Long customerId);

    Customer findById(Long customerId);

    List<Customer> findAll();

    Customer findOne(Customer record);

    List<Customer> findList(Customer record);

    List<Customer> findPageList(PageModel<Customer> page, Customer customer) throws Exception;

    List<Customer> findListByMapLimit(HashMap<String,Object> hashMap, long first, long last);

    long findCount(Customer customer) throws Exception;

    long findCountByMap(HashMap<String,Object> hashMap);

}
