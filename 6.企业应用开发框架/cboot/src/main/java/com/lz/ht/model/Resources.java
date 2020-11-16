package com.lz.ht.model;
import java.lang.*;
import java.sql.*;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Resources {
        private Long id;
        /** 资源名称 */
        private String name;
        /** 资源id */
        private Long resKey;
        /** 资源链接 */
        private String resUrl;
        /** 父资源id */
        private Long presKey;
        /** 父资源名称 */
        @JsonProperty("pName")
        private String pName;
        /** 资源排序 */
        private Long sort;
        /** 0 启用 1 禁用 */
        private Long type;
        /** 资源类型0显式资源1非显式资源 */
        private Long resType;
        /** 资源图标*/
        private String resIcon;
        

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

        public void setResKey(Long resKey){
            this.resKey = resKey;
        }

        public Long getResKey() {
            return this.resKey;
        }

        public void setResUrl(String resUrl){
            this.resUrl = resUrl;
        }

        public String getResUrl() {
            return this.resUrl;
        }

        public void setPresKey(Long presKey){
            this.presKey = presKey;
        }

        public Long getPresKey() {
            return this.presKey;
        }

        public void setPName(String pName){
            this.pName = pName;
        }

        public String getPName() {
            return this.pName;
        }

        public void setSort(Long sort){
            this.sort = sort;
        }

        public Long getSort() {
            return this.sort;
        }

        public void setType(Long type){
            this.type = type;
        }

        public Long getType() {
            return this.type;
        }

        
        
		public Long getResType() {
			return resType;
		}

		public void setResType(Long resType) {
			this.resType = resType;
		}
		

		public String getResIcon() {
			return resIcon;
		}

		public void setResIcon(String resIcon) {
			this.resIcon = resIcon;
		}

		public Resources() {
			 
		}

		public Resources(String name, Long resKey, String resUrl, Long presKey, String pName, Long sort, Long type) {
			super();
			this.name = name;
			this.resKey = resKey;
			this.resUrl = resUrl;
			this.presKey = presKey;
			this.pName = pName;
			this.sort = sort;
			this.type = type; 
		}



}