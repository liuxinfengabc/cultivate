package com.lz.ht.model;
import java.lang.*;
import java.sql.*;
import java.util.Date;

public class Fkeys {
        /** ID */
        private Long id;
        /** 外键名称 */
        private String fkName;
        /** 主表名 */
        private String mTableName;
        /** 主表关联列名 */
        private String mColumnName;
        /** 从表名 */
        private String rTableName;
        /** 从表关联列名 */
        private String rColumnName;
        /** 关联查询类型<!-- "{\"formType\":\"select\",\"options\":[{\"optText\":\"一对一\",\"optValue\":0},{\"optText\":\"列表\",\"optValue\":1},{\"optText\":\"树\",\"optValue\":2}]}" --> */
        private Long rType;
        /** 关联查询语句 */
        private String rSql;
        /** 主表取值查询视图的列对应 */
        private String coverOtherValueColumn;

        public void setId(Long id){
            this.id = id;
        }

        public Long getId() {
            return this.id;
        }

        public void setFkName(String fkName){
            this.fkName = fkName;
        }

        public String getFkName() {
            return this.fkName;
        }

        public void setMTableName(String mTableName){
            this.mTableName = mTableName;
        }

        public String getMTableName() {
            return this.mTableName;
        }

        public void setMColumnName(String mColumnName){
            this.mColumnName = mColumnName;
        }

        public String getMColumnName() {
            return this.mColumnName;
        }

        public void setRTableName(String rTableName){
            this.rTableName = rTableName;
        }

        public String getRTableName() {
            return this.rTableName;
        }

        public void setRColumnName(String rColumnName){
            this.rColumnName = rColumnName;
        }

        public String getRColumnName() {
            return this.rColumnName;
        }

        public void setRType(Long rType){
            this.rType = rType;
        }

        public Long getRType() {
            return this.rType;
        }

        public void setRSql(String rSql){
            this.rSql = rSql;
        }

        public String getRSql() {
            return this.rSql;
        }

        public void setCoverOtherValueColumn(String coverOtherValueColumn){
            this.coverOtherValueColumn = coverOtherValueColumn;
        }

        public String getCoverOtherValueColumn() {
            return this.coverOtherValueColumn;
        }



}