package com.lz.ht.model;
import java.lang.*;
import java.sql.*;
import java.util.Date;

public class User {
        /** ID */
        private Long id;
        /** 名称 */
        private String userName;
        /** 密码 */
        private String password;
        /** 状态0启用1禁用<!-- "{\"formType\":\"select\",\"options\":[{\"optText\":\"启用\",\"optValue\":0},{\"optText\":\"禁用\",\"optValue\":1}]}" --> */
        private Long enable;
        /** 手机号码 */
        private String phoneNo;
        /** 邮箱 */
        private String mail;
        /** 添加时间<!-- "{\"formType\":\"datePicker\"}" --> */
        private java.sql.Date addTime;
        /** 部门ID<!-- "{\"formType\":\"foreignKey\",\"fKName\":\"user_dept_fk\"}" --> */
        private Long deptId;
        /** 部门名称 */
        private String deptName;
        /** 性别 */
        private Long sex;
        /** 简介 */
        private String info;

        public void setId(Long id){
            this.id = id;
        }

        public Long getId() {
            return this.id;
        }

        public void setUserName(String userName){
            this.userName = userName;
        }

        public String getUserName() {
            return this.userName;
        }

        public void setPassword(String password){
            this.password = password;
        }

        public String getPassword() {
            return this.password;
        }

        public void setEnable(Long enable){
            this.enable = enable;
        }

        public Long getEnable() {
            return this.enable;
        }

        public void setPhoneNo(String phoneNo){
            this.phoneNo = phoneNo;
        }

        public String getPhoneNo() {
            return this.phoneNo;
        }

        public void setMail(String mail){
            this.mail = mail;
        }

        public String getMail() {
            return this.mail;
        }

        public void setAddTime(java.sql.Date addTime){
            this.addTime = addTime;
        }

        public java.sql.Date getAddTime() {
            return this.addTime;
        }

        public void setDeptId(Long deptId){
            this.deptId = deptId;
        }

        public Long getDeptId() {
            return this.deptId;
        }

        public void setDeptName(String deptName){
            this.deptName = deptName;
        }

        public String getDeptName() {
            return this.deptName;
        }

        public void setSex(Long sex){
            this.sex = sex;
        }

        public Long getSex() {
            return this.sex;
        }

        public void setInfo(String info){
            this.info = info;
        }

        public String getInfo() {
            return this.info;
        }



}