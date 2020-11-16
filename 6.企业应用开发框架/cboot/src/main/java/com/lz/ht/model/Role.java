package com.lz.ht.model;
import java.lang.*;
import java.sql.*;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Role {
        /** id */
        private Long id;
        /** 角色id */
        private String roleKey;
        /** 角色描述 */
        private String roleDesc;
        /** 父角色id */
        @JsonProperty("pRoleKey")
        private String pRoleKey;
        /** 父角色描述 */
        @JsonProperty("pRoleDesc")
        private String pRoleDesc;
        /** 0 启用 1 禁用 */
        private Long enable;
        /** 排序 */
        private Long sort;

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

        public void setRoleDesc(String roleDesc){
            this.roleDesc = roleDesc;
        }

        public String getRoleDesc() {
            return this.roleDesc;
        }

        public void setPRoleKey(String pRoleKey){
            this.pRoleKey = pRoleKey;
        }

        public String getPRoleKey() {
            return this.pRoleKey;
        }

        public void setPRoleDesc(String pRoleDesc){
            this.pRoleDesc = pRoleDesc;
        }

        public String getPRoleDesc() {
            return this.pRoleDesc;
        }

        public void setEnable(Long enable){
            this.enable = enable;
        }

        public Long getEnable() {
            return this.enable;
        }

        public void setSort(Long sort){
            this.sort = sort;
        }

        public Long getSort() {
            return this.sort;
        }



}