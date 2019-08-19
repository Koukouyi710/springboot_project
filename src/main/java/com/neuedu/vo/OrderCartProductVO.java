package com.neuedu.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class OrderCartProductVO implements Serializable{
    private List<OrderItemVO> orderItemVoList;
    private String imageHost;
    private BigDecimal productTotalPrice;

    public List<OrderItemVO> getOrderItemVoList() {
        return orderItemVoList;
    }

    public void setOrderItemVoList(List<OrderItemVO> orderItemVoList) {
        this.orderItemVoList = orderItemVoList;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }

    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }
}
