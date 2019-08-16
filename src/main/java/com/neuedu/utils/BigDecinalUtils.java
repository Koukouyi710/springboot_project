package com.neuedu.utils;


import java.math.BigDecimal;

/**
 * 价格工具类
 */
public class BigDecinalUtils {

    /**
     * 加法计算
     */
    public static BigDecimal add(double d1,double d2){
        BigDecimal bigDecimal_1 = new BigDecimal(String.valueOf(d1));
        BigDecimal bigDecimal_2 = new BigDecimal(String.valueOf(d2));
        return bigDecimal_1.add(bigDecimal_2);
    }
    /**
     * 减法计算
     */
    public static BigDecimal sub(double d1,double d2){
        BigDecimal bigDecimal_1 = new BigDecimal(String.valueOf(d1));
        BigDecimal bigDecimal_2 = new BigDecimal(String.valueOf(d2));
        return bigDecimal_1.subtract(bigDecimal_2);
    }
    /**
     * 乘法计算
     */
    public static BigDecimal mul(double d1,double d2){
        BigDecimal bigDecimal_1 = new BigDecimal(String.valueOf(d1));
        BigDecimal bigDecimal_2 = new BigDecimal(String.valueOf(d2));
        return bigDecimal_1.multiply(bigDecimal_2);
    }
    /**
     * 除法计算,保留两位，四舍五入
     */
    public static BigDecimal div(double d1,double d2){
        BigDecimal bigDecimal_1 = new BigDecimal(String.valueOf(d1));
        BigDecimal bigDecimal_2 = new BigDecimal(String.valueOf(d2));
        return bigDecimal_1.divide(bigDecimal_2,2,BigDecimal.ROUND_HALF_UP);
    }
}
