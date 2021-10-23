package com.lz.ht.model;
import java.lang.*;
import java.sql.*;
import java.util.Date;

public class Orgnews {
        /** ID */
        private Long id;
        /** 新闻类型0内部使用1外部链接 */
        private Long newsType;
        /** 新闻标题 */
        private String ntitle;
        /** 新闻内容 */
        private String ncontent;
        /** 添加时间 */
        private  java.util.Date publishTime;
        /** 发表用户ID */
        private Long userId;
        /** 发表人 */
        private String userName;
        /** 静态路径 */
        private String newsPath;
        /** 状态0起草1发布 */
        private Long nstate;
        /** 是否开启评论功能0开启1关闭*/
        private Long allowComment;
        
        public void setId(Long id){
            this.id = id;
        }

        public Long getId() {
            return this.id;
        }

        public void setNewsType(Long newsType){
            this.newsType = newsType;
        }

        public Long getNewsType() {
            return this.newsType;
        }

        public void setNtitle(String ntitle){
            this.ntitle = ntitle;
        }

        public String getNtitle() {
            return this.ntitle;
        }

        public void setNcontent(String ncontent){
            this.ncontent = ncontent;
        }

        public String getNcontent() {
            return this.ncontent;
        }

        public void setPublishTime(java.util.Date publishTime){
            this.publishTime = publishTime;
        }

        public java.util.Date getPublishTime() {
            return this.publishTime;
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

        public void setNewsPath(String newsPath){
            this.newsPath = newsPath;
        }

        public String getNewsPath() {
            return this.newsPath;
        }

        public void setNstate(Long nstate){
            this.nstate = nstate;
        }

        public Long getNstate() {
            return this.nstate;
        }

		public Long getAllowComment() {
			return allowComment;
		}

		public void setAllowComment(Long allowComment) {
			this.allowComment = allowComment;
		}



}