package com.lz.ht.dao;

import com.lz.ht.model.Customer;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.HashMap;
import org.apache.ibatis.annotations.Mapper;

//@Repository
@Mapper
public interface CustomerMapper {

    void add(Customer record);

    void updateById(Customer record);

    int deleteById(Long CustomerId);

    Customer  findById(Long id);

    List<Customer> findAll();

    List<Customer> findList(Customer record);

    long findCount(Customer customer);

    long findCountByMap(HashMap<String,Object> hashMap);

    List<Customer> findListByMapLimit(HashMap<String,Object> hashMap);

}
