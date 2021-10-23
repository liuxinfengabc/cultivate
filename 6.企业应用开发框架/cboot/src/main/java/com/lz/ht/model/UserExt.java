package com.lz.ht.model;
import java.lang.*;
import java.sql.*;
import java.util.Date;

import lombok.Data;
@Data
public class UserExt {
        /** ID */
        private Long id;
        /** 用户ID */
        private Long userId;
        /** 身份证号 */
        private String personCard;
        /** 用户头像路径 */
        private String userPic;
        /** 生日 */
        private java.sql.Date birthday;
        /** 更新时间 */
        private  java.sql.Timestamp updateTime;

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

        public void setPersonCard(String personCard){
            this.personCard = personCard;
        }

        public String getPersonCard() {
            return this.personCard;
        }

        public void setUserPic(String userPic){
            this.userPic = userPic;
        }

        public String getUserPic() {
            return this.userPic;
        }

        public void setBirthday(java.sql.Date birthday){
            this.birthday = birthday;
        }

        public java.sql.Date getBirthday() {
            return this.birthday;
        }

        public void setUpdateTime(java.sql.Timestamp updateTime){
            this.updateTime = updateTime;
        }

        public java.sql.Timestamp getUpdateTime() {
            return this.updateTime;
        }



}