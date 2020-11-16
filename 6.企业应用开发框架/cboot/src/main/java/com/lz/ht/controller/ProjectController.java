package com.lz.ht.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lz.ht.constant.SysConstant;
import com.lz.ht.dao.CostProjectMapper;
import com.lz.ht.model.CostProject;
import com.lz.ht.model.Dept;
import com.lz.ht.model.User;
import com.lz.ht.page.PageModel;
import com.lz.ht.result.Result;
import com.lz.ht.service.CostProjectService;
import com.lz.ht.service.UserService;
import com.lz.ht.service.serviceImpl.CostProjectServiceImpl;
import com.lz.ht.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
@Slf4j
@Controller
public class ProjectController {

    @Autowired
    private CostProjectMapper costProjectMapper;

    @Autowired
    private CostProjectService costProjectServiceImpl;

//    @Autowired
//    private CostProjectService costProjectService;

    @RequestMapping(value = "/project/list",method = {RequestMethod.GET})
    public String project_list()throws Exception{
        return "project/project_list";
    }


    @RequestMapping(value = "/project/list",method = {RequestMethod.POST})
    @ResponseBody
//    public PageModel list(User user, PageModel<User> page)throws Exception{
//        page.init();
//        List<User> list = userServiceImpl.findPageList(page,user);
//        long count = userServiceImpl.findCount(user);
//        page.packData(count,list);
//        return page;

//    }
    public List<CostProject> queryAllUser() {

        return costProjectMapper.findAll();
    }


    @RequestMapping(value = "/project/queryAllUserByPage",method = {RequestMethod.POST})
    @ResponseBody
    public PageInfo<CostProject>  queryAllUserByPage(CostProject costProject,int currentPageNum,int pageSize)throws Exception{
        //[pageNum, pageSize]  页码  每页显示数量
        PageHelper.startPage(currentPageNum,pageSize);
        PageInfo<CostProject> pageInfo = new PageInfo<>(costProjectMapper.findAll());
        return pageInfo;
    }

    @RequestMapping(value = "/project/add",method = {RequestMethod.GET})
    public String addInit(CostProject costProject, Model model){
        return "project/project_add";
    }

    @RequestMapping(value = "/project/add",method = {RequestMethod.POST})
    @ResponseBody
    public Result add(CostProject costProject){
//        String password = MD5Util.getMD5(SysConstant.INIT_PASSWORD);
//        user.setPassword(password);
        costProjectServiceImpl.insert(costProject);
        return Result.genSuccessResult();
    }

//    @RequestMapping(value = "/project/update",method = {RequestMethod.GET})
//    public String updateInit(CostProject costProject, Model model){
//        dept = costProjectServiceImpl.updateByPrimaryKey(costProject.getProjectId());
//        model.addAttribute("dept",dept);
//        return "dept/dept_update";
//    }

    @RequestMapping(value = "/project/delete",method = {RequestMethod.POST})
    @ResponseBody
//    public Result delete(CostProject costProject){
//        //查询，此用户的角色，将此表t_user_role用户的角色一并删除
//        costProjectServiceImpl.deleteUserCascade(costProject.getId());
//        return Result.genSuccessResult();
//    }
//    public String delete(@RequestParam(name="id") Integer id){
//        costProjectServiceImpl.deleteByPrimaryKey(id);
//        return "success";
//    }
    public Result delete(CostProject costProject){
        costProjectServiceImpl.deleteByPrimaryKey(costProject.getProjectId());
        return Result.genSuccessResult();
    }

    @RequestMapping(value = "/project/update",method = {RequestMethod.GET})
    public String updateInit(CostProject costProject,Model model){
        System.out.println(costProject.getProjectId());
        costProject = costProjectServiceImpl.findById(costProject.getProjectId());
        model.addAttribute("costProject",costProject);
        return "project/project_update";
    }

    @RequestMapping(value = "/project/update",method = {RequestMethod.POST})
    @ResponseBody
    public Result update(CostProject costproject){
        costProjectServiceImpl.updateByPrimaryKey(costproject);
        return Result.genSuccessResult();
    }

}
