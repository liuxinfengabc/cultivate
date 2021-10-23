package com.lz.ht.quartz;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.ArrayList;
import java.util.List;

/**
 *上传图片到分公司的定时任务类
 */
@Slf4j
public class UploadPictures2ProvinceJob implements Job {


    private final Integer UPLOAD_PER_SIZE = 10 ;//每次上传UPLOAD_PER_SIZE个单据的图片


    public void uploadPicturesExported(){
         long startTime = System.currentTimeMillis();
         log.info("uploadPicturesExported" );
        long endTime = System.currentTimeMillis();
        long timeLong = endTime - startTime;
        double timeDouble= Double.parseDouble(Long.toString(timeLong));
        log.info("uploadPicturesExported执行时间为" + timeDouble+ "毫秒，即" + timeDouble/(double)1000 + "秒");
    }




    private    <T> List<List<T>> split(List<T> resList,int count){
        if(resList==null ||count<1)
            return  null ;
        List<List<T>> ret=new ArrayList<List<T>>();
        int size=resList.size();
        if(size<=count){ //数据量不足count指定的大小
            ret.add(resList);
        }else{
            int pre=size/count;
            int last=size%count;
            //前面pre个集合，每个大小都是count个元素
            for(int i=0;i<pre;i++){
                List<T> itemList=new ArrayList<T>();
                for(int j=0;j<count;j++){
                    itemList.add(resList.get(i*count+j));
                }
                ret.add(itemList);
            }
            //last的进行处理
            if(last>0){
                List<T> itemList=new ArrayList<T>();
                for(int i=0;i<last;i++){
                    itemList.add(resList.get(pre*count+i));
                }
                ret.add(itemList);
            }
        }
        return ret;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        uploadPicturesExported();
    }

    private void before(){
        log.info("任务开始执行before");
    }

    private void after(){
        log.info("任务执行after");
    }
}
