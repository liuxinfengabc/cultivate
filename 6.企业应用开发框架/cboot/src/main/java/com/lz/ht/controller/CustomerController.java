package com.lz.ht.controller;

import com.lz.ht.result.Result;
import com.lz.ht.model.Customer;
import com.lz.ht.page.PageModel;
import com.lz.ht.result.Result;
import com.lz.ht.service.CustomerService;
import com.lz.ht.util.JwtUtil;
import com.lz.ht.base.BaseController;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.google.gson.Gson;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.github.abel533.sql.SqlMapper;
@Slf4j
@Controller
public class CustomerController extends BaseController{

    @Autowired
    private CustomerService customerServiceImpl;


    @RequestMapping(value = "/customer/list",method = {RequestMethod.GET})
    public String customer_list()throws Exception{
        return "customer/customer_list";
    }



    @RequestMapping(value = "/customer/list",method = {RequestMethod.POST})
    @ResponseBody
    public PageModel list(Customer customer, PageModel<Customer> page)throws Exception{
           page.init();
           List<Customer> list = customerServiceImpl.findPageList(page,customer);
           long count = customerServiceImpl.findCount(customer);
           page.packData(count,list);
           return page;
    }

    @RequestMapping(value = "/customer/add",method = {RequestMethod.GET})
    public String addInit(Customer customer,Model model){
        return "customer/customer_add";
    }

    @RequestMapping(value = "/customer/add",method = {RequestMethod.POST})
    @ResponseBody
    public Result add(Customer customer){
        customerServiceImpl.add(customer);
        return Result.genSuccessResult();
    }

    @RequestMapping(value = "/customer/update",method = {RequestMethod.GET})
    public String updateInit(Customer customer,Model model){
        customer = customerServiceImpl.findById(customer.getId());
        model.addAttribute("customer",customer);
        return "customer/customer_update";
    }

    @RequestMapping(value = "/customer/update",method = {RequestMethod.POST})
    @ResponseBody
    public Result update(Customer customer){
        customerServiceImpl.updateById(customer);
        return Result.genSuccessResult();
    }

    @RequestMapping(value = "/customer/delete",method = {RequestMethod.POST})
    @ResponseBody
    public Result delete(Customer customer){
        customerServiceImpl.deleteById(customer.getId());
        return Result.genSuccessResult();
    }



    @Autowired
    private SqlMapper sqlMapper;

    private Gson gson = new Gson();

    @RequestMapping(value = "/customer/selectPageList",method = {RequestMethod.GET})
    public String selectPageList(){
        return "customer/customer_selectPageList";
    }

    @RequestMapping(value = "/customer/selectPageList",method = {RequestMethod.POST})
    @ResponseBody
    public PageModel selectPageList(Customer customer, PageModel<Customer> page)throws Exception{
        page.init();
        List<Customer> list = customerServiceImpl.findPageList(page,customer);
        long count = customerServiceImpl.findCount(customer);
        page.packData(count,list);
        return page;
    }


    @RequestMapping(value = "/customer/choosePage",method = {RequestMethod.GET})
    public String selectPage(String fkName,Model model){
        if(StringUtils.isNotEmpty(fkName)){
            Map<String, Object> foreignKeyMap = sqlMapper.selectOne("select * from t_fkeys where fkName = \'"+ fkName+"\'" );
            String rSql = foreignKeyMap.get("rSql").toString();
            log.info("rSql:{}",rSql);
            List<Map<String, Object>> relativeMapList = sqlMapper.selectList(rSql);
            if(relativeMapList!=null){
                model.addAttribute("relativeMapList",relativeMapList);
                String rType = foreignKeyMap.get("rType").toString();
                if("0".equals(rType)) return select_radioPage(model);
                if("1".equals(rType)) return select_radioPage(model);
                if("2".equals(rType)) {
                    String coverOtherValueColumn = foreignKeyMap.get("coverOtherValueColumn").toString();
                    model.addAttribute("coverOtherValueColumn",coverOtherValueColumn);
                    model.addAttribute("treeNodes",toJson(relativeMapList));
                    return select_zTreePage(model);
                }
            }else{
                return "error/error";
            }
        }
        return "error/error";
    }






    private String select_radioPage(Model model){
        model.addAttribute("demo","demoValue");
        return "customer/customer_selectPageList";
    }

    private String select_zTreePage(Model model){
        model.addAttribute("demo","demoValue");
        return "customer/customer_foreignKeyTree";
    }
    public String toJson(Object obj){
        String s = gson.toJson(obj);
        return s;
    }


}
