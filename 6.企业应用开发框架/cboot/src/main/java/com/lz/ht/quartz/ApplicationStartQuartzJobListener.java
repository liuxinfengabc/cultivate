package com.lz.ht.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

@Slf4j
@Configuration
public class ApplicationStartQuartzJobListener implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 初始启动quartz
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            QuartzScheduler quartzScheduler = SpringContextHolder.getBean(QuartzScheduler.class);
            quartzScheduler.startJob();
        } catch (SchedulerException e) {
            log.info("ApplicationStartQuartzJobListener 启动失败！");
        }
    }


    @Bean
    public QuartzScheduler getQuartzScheduler() throws SchedulerException {
        SchedulerFactory schedulerFactoryBean = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        QuartzScheduler quartzScheduler = new QuartzScheduler();
        quartzScheduler.setScheduler(scheduler);
        return  quartzScheduler;
    }



}