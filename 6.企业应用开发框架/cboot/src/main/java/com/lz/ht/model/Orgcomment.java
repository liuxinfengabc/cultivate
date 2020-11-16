package com.lz.ht.model;
import java.lang.*;

import com.fasterxml.jackson.annotation.JsonFormat; 

public class Orgcomment {
        /** ID */
        private Long id;
        /** 新闻ID */
        private Long newsId;
        /** 评论内容 */
        private String ncomment;
        /** 评论人ID */
        private Long userId;
        /** 评论人名称 */
        private String userName;
        /** 评论时间 */
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
        private  java.util.Date addTime;
        /** 评论状态0显示1隐藏 */
        private Long cstate;

        public void setId(Long id){
            this.id = id;
        }

        public Long getId() {
            return this.id;
        }

        public void setNewsId(Long newsId){
            this.newsId = newsId;
        }

        public Long getNewsId() {
            return this.newsId;
        }

        public void setNcomment(String ncomment){
            this.ncomment = ncomment;
        }

        public String getNcomment() {
            return this.ncomment;
        }

        public void setUserId(Long userId){
            this.userId = userId;
        }

        public Long getUserId() {
            return this.userId;
        }

        public void setUserName(String userName){
            this.userName = userName;
        }

        public String getUserName() {
            return this.userName;
        }

        public void setAddTime(java.util.Date addTime){
            this.addTime = addTime;
        }

        public java.util.Date getAddTime() {
            return this.addTime;
        }

        public void setCstate(Long cstate){
            this.cstate = cstate;
        }

        public Long getCstate() {
            return this.cstate;
        }



}