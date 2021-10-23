package com.lz.ht.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 这里并没有采用restful风格 只是简单封装了一下api
 */
@Slf4j
@Controller
@RequestMapping("/quartz")
public class QuartzApiController  {
    @Autowired
    private QuartzScheduler quartzScheduler;

    /***
     * 启动所有任务
     * @return
     */
    @RequestMapping("/start")
    @ResponseBody
    public Object startQuartzJob() {
        log.info("QuartzApiController.startQuartzJob:");
        try {
            quartzScheduler.startJob();
        } catch (SchedulerException e) {
            log.info("操作失败");
            throw new RuntimeException("操作失败，请重试！");
        }
        return  new Object();
    }



    /***
     * 获取任务信息
     * @param name
     * @param group
     * @return
     */
    @RequestMapping("/info")
    @ResponseBody
    public Object getQuartzJob(String name, String group) {
        log.info("QuartzApiController.getQuartzJob:name:{},group:{}",name,group);
        String info = null;
        try {
            info = quartzScheduler.getJobInfo(name, group);
        } catch (SchedulerException e) {
            log.info("操作失败");
            throw new RuntimeException("操作失败，请重试！");
        }
        return new Object();
    }

    /***
     * 修改任务执行时间
     * @param time
     * @return
     */
    @RequestMapping("/modifyUploadPicturesJob")
    @ResponseBody
    public Object modifyUploadPicturesJob( String time) {
        String name = "uploadPictures2ProvinceJob";
        String group = "group1";
        //time = AESUtil.decrypt(time);
        log.info("QuartzApiController.modifyQuartzJob:name:{},group:{},time:{}",name,group,time);
        try {
            quartzScheduler.modifyJob(name, group, time);
        } catch (SchedulerException e) {
            log.info("操作失败");
            throw new RuntimeException("操作失败，请重试！");
        }
        return new Object();
    }


    /***
     * 停止任务执行
     * @param name
     * @param group
     * @return
     */
    @RequestMapping(value = "/pause")
    @ResponseBody
    public Object pauseQuartzJob(String name, String group) {
        log.info("QuartzApiController.pauseQuartzJob:name:{},group:{}",name,group);
        try {
            quartzScheduler.pauseJob(name, group);
        } catch (SchedulerException e) {
            log.info("操作失败");
            throw new RuntimeException("操作失败，请重试！");
        }
        return  new  Object();
    }

    /***
     * 停止所有任务执行
     * @return
     */
    @RequestMapping(value = "/pauseAll")
    @ResponseBody
    public Object pauseAllQuartzJob() {
        log.info("QuartzApiController.pauseAllQuartzJob:name:{},group:{}");
        try {
            quartzScheduler.pauseAllJob();
        } catch (SchedulerException e) {
            log.info("操作失败");
            throw new RuntimeException("操作失败，请重试！");
        }
        return  new  Object();
    }

    /***
     * 删除任务
     * @param name
     * @param group
     * @return
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object deleteJob(String name, String group) {
        log.info("QuartzApiController.deleteJob:name:{},group:{}",name,group);
        try {
            quartzScheduler.deleteJob(name, group);
        } catch (SchedulerException e) {
            log.info("操作失败");
            throw new RuntimeException("操作失败，请重试！");
        }
        return  new  Object();
    }

    /**
     * 恢复所有任务
     */
    @RequestMapping(value = "/resumeAllJob")
    @ResponseBody
    public Object resumeAllJob() {
        try {
            quartzScheduler.resumeAllJob();
        } catch (SchedulerException e) {
            log.info("操作失败");  throw new RuntimeException("操作失败，请重试！");
        }
        return  new  Object();
    }

    /**
     * 恢复某个任务
     */
    @RequestMapping(value = "/resumeJob")
    @ResponseBody
    public Object resumeJob(String name, String group){
        try {
            quartzScheduler.resumeJob(name,group);
        } catch (SchedulerException e) {
            log.info("操作失败");   throw new RuntimeException("操作失败，请重试！");
        }
        return  new  Object();
    }


}
