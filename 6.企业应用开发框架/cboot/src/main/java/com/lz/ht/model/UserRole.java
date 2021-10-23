package com.lz.ht.model;
import java.lang.*;
import java.sql.*;
import java.util.Date;

public class UserRole {
        /** id */
        private Long id;
        /** 用户id */
        private Long userId;
        /** 角色识别key */
        private String roleKey;

        public void setId(Long id){
            this.id = id;
        }

        public Long getId() {
            return this.id;
        }

        public void setUserId(Long userId){
            this.userId = userId;
        }

        public Long getUserId() {
            return this.userId;
        }

        public void setRoleKey(String roleKey){
            this.roleKey = roleKey;
        }

        public String getRoleKey() {
            return this.roleKey;
        }



}