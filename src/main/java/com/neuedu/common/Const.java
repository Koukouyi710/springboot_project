package com.neuedu.common;

public class Const {

    public static final String CURRENT_USER="user";
    public static final String CURRENT_ADDR="/uploadpic/";
    public enum RoleEnum{

        ROLE_ADMIN(0,"管理员"),
        ROLE_CUSTOMER(1,"普通用户")
        ;
        private int code;
        private String desc;
        private RoleEnum(int code,String desc){
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public enum ResponseCodeEunm{

        NEED_LOGIN(2,"需要登录"),
        NO_PRIVILEGE(3,"没有权限")
        ;

        private int code;
        private String desc;
        private ResponseCodeEunm(int code,String desc){
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public enum ProductStatusEunm{

        PRODUCT_ONLINE(1,"在售"),
        PRODUCT_OFFLINE(2,"下架"),
        PRODUCT_DELETE(3,"删除")
        ;

        private int code;
        private String desc;
        private ProductStatusEunm(int code,String desc){
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public enum CartStatusEunm{

        PRODUCT_CHECKED(1,"已勾选"),
        PRODUCT_UNCHECKED(0,"未勾选")
        ;

        private int code;
        private String desc;
        private CartStatusEunm(int code,String desc){
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
