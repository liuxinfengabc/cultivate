package com.lz.ht.model;
import java.lang.*;
import java.sql.*;
import java.util.Date;

public class Dept {
        /** id */
        private Long id;
        /** 部门名称 */
        private String deptName;
        /** 部门描述 */
        private String deptDesc;
        /** 0 启用 1 禁用<!-- "{\"formType\":\"select\",\"options\":[{\"optText\":\"启用\",\"optValue\":0},{\"optText\":\"禁用\",\"optValue\":1}]}" --> */
        private Long enable;
        /** 上级部们<!-- "{\"formType\":\"foreignKey\",\"fKName\":\"dept_dept_fk\"}" --> */
        private Long parentId;
        /** 排序 */
        private Long sort;
        /** 上级部们名称 */
        private String parentName;

        public void setId(Long id){
            this.id = id;
        }

        public Long getId() {
            return this.id;
        }

        public void setDeptName(String deptName){
            this.deptName = deptName;
        }

        public String getDeptName() {
            return this.deptName;
        }

        public void setDeptDesc(String deptDesc){
            this.deptDesc = deptDesc;
        }

        public String getDeptDesc() {
            return this.deptDesc;
        }

        public void setEnable(Long enable){
            this.enable = enable;
        }

        public Long getEnable() {
            return this.enable;
        }

        public void setParentId(Long parentId){
            this.parentId = parentId;
        }

        public Long getParentId() {
            return this.parentId;
        }

        public void setSort(Long sort){
            this.sort = sort;
        }

        public Long getSort() {
            return this.sort;
        }

        public void setParentName(String parentName){
            this.parentName = parentName;
        }

        public String getParentName() {
            return this.parentName;
        }



}