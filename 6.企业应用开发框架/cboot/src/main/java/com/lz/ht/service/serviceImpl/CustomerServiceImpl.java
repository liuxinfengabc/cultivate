package com.lz.ht.service.serviceImpl;

import com.lz.ht.dao.CustomerMapper;
import com.lz.ht.model.Customer;
import com.lz.ht.service.CustomerService;
import com.lz.ht.page.PageModel;
import com.lz.ht.util.ToolKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.apache.ibatis.exceptions.TooManyResultsException;
import java.util.List;
import java.util.HashMap;

@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerServiceImpl  implements  CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public void add(Customer record) {
        customerMapper.add(record);
    }

    @Override
    public void updateById(Customer record) {
        customerMapper.updateById(record);
    }

    @Override
    public int deleteById(Long CustomerId) {
        return  customerMapper.deleteById(CustomerId);
    }

    @Override
    public Customer findById(Long CustomerId) {
        return  customerMapper.findById(CustomerId);
    }

    @Override
    public List<Customer> findAll() {
        return customerMapper.findAll();
    }


    @Override
    public Customer findOne(Customer record) {
        List<Customer>  list = customerMapper.findList(record);
        if ((list!=null)&&(list.size() == 1)) {
            return list.get(0);
        } else if ((list!=null)&&(list.size() > 1)) {
            throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
        } else {
            return null;
        }
    }

    @Override
    public List<Customer> findList(Customer record){
         return  customerMapper.findList(record);
    }

    @Override
    public List<Customer> findPageList(PageModel<Customer> page, Customer customer) throws Exception {
        HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(customer);
        List<Customer> pageList = findListByMapLimit(hashMap, page.getMsFirst(), page.getMsLast());
        return pageList;
    }

    @Override
    public List<Customer> findListByMapLimit(HashMap<String,Object> hashMap,long first,long last){
        hashMap.put("msFirst",first);
        hashMap.put("msLast",last);
        return customerMapper.findListByMapLimit(hashMap);
    }

    @Override
    public long findCount(Customer  customer) throws Exception {
        HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(customer);
        return  findCountByMap(hashMap);
    }

    @Override
    public long findCountByMap(HashMap<String,Object> hashMap){
        return customerMapper.findCountByMap(hashMap);
    }


 }
