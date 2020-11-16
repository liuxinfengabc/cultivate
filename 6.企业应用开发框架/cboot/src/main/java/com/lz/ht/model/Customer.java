package com.lz.ht.model;
import java.lang.*;
import java.sql.*;
import java.util.Date;

import lombok.Data;
@Data
public class Customer {
        /** ID */
	    private Long id;
        /** 客户名称 */
        private String custName;
        /** 电话 */
        private String cPhoneNum;
        /** 电子邮箱 */
        private String cEmail;
        /** 地址 */
        private String cAddress;
        /** 备注 */
        private String cRemark;
        /** 添加时间 */
        private  java.sql.Timestamp addTime;
        /** 状态0启用1删除<!-- "{\"formType\":\"select\",\"options\":[{\"optText\":\"启用\",\"optValue\":0},{\"optText\":\"删除\",\"optValue\":1}]}" --> */
        private Long cStatus;

        public void setCustName(String custName){
            this.custName = custName;
        }

        public String getCustName() {
            return this.custName;
        }

        public void setCPhoneNum(String cPhoneNum){
            this.cPhoneNum = cPhoneNum;
        }

        public String getCPhoneNum() {
            return this.cPhoneNum;
        }

        public void setCEmail(String cEmail){
            this.cEmail = cEmail;
        }

        public String getCEmail() {
            return this.cEmail;
        }

        public void setCAddress(String cAddress){
            this.cAddress = cAddress;
        }

        public String getCAddress() {
            return this.cAddress;
        }

        public void setCRemark(String cRemark){
            this.cRemark = cRemark;
        }

        public String getCRemark() {
            return this.cRemark;
        }

        public void setAddTime(java.sql.Timestamp addTime){
            this.addTime = addTime;
        }

        public java.sql.Timestamp getAddTime() {
            return this.addTime;
        }

        public void setCStatus(Long cStatus){
            this.cStatus = cStatus;
        }

        public Long getCStatus() {
            return this.cStatus;
        }



}