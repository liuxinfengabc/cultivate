package com.lz.ht.model;
import java.lang.*;
import java.sql.*;
import java.util.Date;

public class SysConfig {
        /** ID */
        private Long id;
        /** 名称 */
        private String name;
        /** 值 */
        private String value;
        /** 配置索引 */
        private Long sysConfigIndex;
        /** 配置状态0启用1禁用 */
        private Integer delStatus;

        public void setId(Long id){
            this.id = id;
        }

        public Long getId() {
            return this.id;
        }

        public void setName(String name){
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public void setValue(String value){
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public void setSysConfigIndex(Long sysConfigIndex){
            this.sysConfigIndex = sysConfigIndex;
        }

        public Long getSysConfigIndex() {
            return this.sysConfigIndex;
        }

        public void setDelStatus(Integer delStatus){
            this.delStatus = delStatus;
        }

        public Integer getDelStatus() {
            return this.delStatus;
        }



}