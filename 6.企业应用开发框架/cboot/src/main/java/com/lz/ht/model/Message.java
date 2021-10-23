package com.lz.ht.model;
import java.lang.*;
import java.sql.*;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Message {
        /** ID */
        private Long id;
        /** 发送人 */
        private Long fromUserId;
        /** 接收人 */
        private Long toUserId;
        /** 消息内容 */
        private String msgContent;
        /** 状态0未读1已读 */
        private Long status;
        /** 添加时间 */
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
        private  java.util.Date addTime;
        /** 阅读时间 */
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
        private  java.util.Date readTime; 
        /** 会话ID */
        private String conversationId;
        /**会话序列*/
        private Integer corder;

        public void setId(Long id){
            this.id = id;
        }

        public Long getId() {
            return this.id;
        }

        public void setFromUserId(Long fromUserId){
            this.fromUserId = fromUserId;
        }

        public Long getFromUserId() {
            return this.fromUserId;
        }

        public void setToUserId(Long toUserId){
            this.toUserId = toUserId;
        }

        public Long getToUserId() {
            return this.toUserId;
        }

        public void setMsgContent(String msgContent){
            this.msgContent = msgContent;
        }

        public String getMsgContent() {
            return this.msgContent;
        }

        public void setStatus(Long status){
            this.status = status;
        }

        public Long getStatus() {
            return this.status;
        }

        public void setAddTime(java.util.Date addTime){
            this.addTime = addTime;
        }

        public java.util.Date getAddTime() {
            return this.addTime;
        }

        public void setReadTime(java.util.Date readTime){
            this.readTime = readTime;
        }

        public java.util.Date getReadTime() {
            return this.readTime;
        }

		public String getConversationId() {
			return conversationId;
		}

		public void setConversationId(String conversationId) {
			this.conversationId = conversationId;
		}

		public Integer getCorder() {
			return corder;
		}

		public void setCorder(Integer corder) {
			this.corder = corder;
		}



}