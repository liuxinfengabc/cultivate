package com.lz.ht.model;
import java.lang.*;
import java.sql.*;
import java.util.Date;

public class RoleResources {
        /** id */
        private Long id;
        /** 角色id */
        private String roleKey;
        /** 资源id */
        private String resKey;

        public void setId(Long id){
            this.id = id;
        }

        public Long getId() {
            return this.id;
        }

        public void setRoleKey(String roleKey){
            this.roleKey = roleKey;
        }

        public String getRoleKey() {
            return this.roleKey;
        }

        public void setResKey(String resKey){
            this.resKey = resKey;
        }

        public String getResKey() {
            return this.resKey;
        }



}