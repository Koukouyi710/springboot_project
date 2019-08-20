package com.neuedu.common;

public class Const {

    public static final String CURRENT_USER="user";
    public static final String CURRENT_ADDR="/uploadpic/";
    public static final String TRADE_SUCCESS = "TRADE_SUCCESS";
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

    public enum OrderStatusEunm{

        /**
         * 订单状态：
         * 0-已取消
         * 10-未付款
         * 20-已付款
         * 40-已发货
         * 50-交易成功
         * 60-交易关闭
         */
        ORDER_CANCELED(0,"已取消"),
        ORDER_UNPAY(10,"未付款"),
        ORDER_PAYED(20,"已付款"),
        ORDER_SEND(40,"已发货"),
        ORDER_SUCCESS(50,"交易成功"),
        ORDER_CLOSED(60,"交易关闭"),
        ;

        private int code;
        private String desc;
        private OrderStatusEunm(int code,String desc){
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

        public static OrderStatusEunm codeOf(Integer code){
            for (OrderStatusEunm orderStatusEunm:values()){
                if (code==orderStatusEunm.getCode()){
                    return orderStatusEunm;
                }
            }
            return null;
        }
    }
    public enum PaymentEunm{


        PAYMENT_ONLINE(1,"线上支付")
        ;

        private int code;
        private String desc;
        private PaymentEunm(int code,String desc){
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

        public static PaymentEunm codeOf(Integer code){
            for (PaymentEunm paymentEunm:values()){
                if (code==paymentEunm.getCode()){
                    return paymentEunm;
                }
            }
            return null;
        }
    }

    public enum PaymentPlatformEunm{


        ALIPAY(1,"支付宝")
        ;

        private int code;
        private String desc;
        private PaymentPlatformEunm(int code,String desc){
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
