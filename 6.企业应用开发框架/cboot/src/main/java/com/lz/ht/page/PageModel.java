package com.lz.ht.page;

import java.util.List;

/**
 * @author Administrator
 */
public class PageModel<T> {


    public static final long DEFAULT_PAGE_SIZE = 10L ;

    /**
     * 当前传入的页面页码，从1开始
     */
    private long currentPageNum;

    /**
     * 总页数
     */
    private long totalPage;

    /**
     * 总条数
     */
    private long totalRecords;

    /**
     * 每页的条数
     */
    private long pageSize;

    /**
     * mysql limit分页第一个参数 从0开始
     */
    private long msFirst;
    /**
     * mysql limit分页第二个参数
     */
    private long msLast;


    /**
     * 当前页的数据封装
     */
    private List<T> recordList;


    public long getMsFirst() {
        return  this.msFirst;
    }

    public void setMsFirst(long msFirst) {
        this.msFirst = msFirst;
    }

    public long getMsLast() {
        return this.msLast;
    }

    public void setMsLast(long msLast) {
        this.msLast = msLast;
        if(this.msLast<0)  this.msLast =  DEFAULT_PAGE_SIZE;
    }

    public void setCurrentPageNum(long currentPageNum){
        this.currentPageNum = currentPageNum;
        if(this.currentPageNum <1L)  this.currentPageNum = 1L;
    }

    public PageModel(){}
    public void init(){
        if(this.currentPageNum <= 0) currentPageNum = 1;
        if(this.pageSize <=0) this.pageSize = DEFAULT_PAGE_SIZE;
        this.msFirst = (this.currentPageNum - 1) * this.pageSize;
        this.msLast  = this.pageSize;
    }

    public PageModel(long currentPageNum,long totalRecords ,List<T> recordList){
        this.currentPageNum  = currentPageNum;
        this.totalRecords = totalRecords;
        this.recordList = recordList;
        setTotalPage();
    }


    public PageModel(long currentPageNum,long totalRecords,long pageSize,List<T> recordList){
        this.currentPageNum  = currentPageNum;
        this.totalRecords = totalRecords;
        this.pageSize = pageSize;
        this.recordList = recordList;
        setTotalPage();
    }


    public void packData(long totalRecords,List<T> recordList){
        this.totalRecords = totalRecords;
        this.recordList = recordList;
        setTotalPage();
    }



    public void setTotalPage(){
        if(totalRecords>0L&&pageSize>0L){
            if(totalRecords%pageSize == 0L){
                this.totalPage = totalRecords/pageSize ;
            }else{
                this.totalPage = (long)(totalRecords/pageSize + 1);
            }
        }else{
            this.totalPage = 0L;
        }
    }


    public PageModel(List<T> list){
        this.recordList = list;
        this.totalPage =1L;
        this.pageSize = list.size();
        this.currentPageNum = 1L;
        this.totalRecords = list.size();
    }


    public long getCurrentPageNum() {
        return currentPageNum;
    }

    public long getTotalPage() {
        return totalPage;
    }


    public long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<T> recordList) {
        this.recordList = recordList;
    }

    public static void main(String[] args) {
        System.out.println((100011L == 1000L));
    }

}
