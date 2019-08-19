package com.neuedu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.*;
import com.neuedu.pojo.*;
import com.neuedu.service.IOrderService;
import com.neuedu.utils.BigDecinalUtils;
import com.neuedu.utils.DateUtils;
import com.neuedu.utils.PropertiesUtils;
import com.neuedu.vo.OrderCartProductVO;
import com.neuedu.vo.OrderItemVO;
import com.neuedu.vo.OrderVO;
import com.neuedu.vo.ShippingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
public class OrderServiceImpl implements IOrderService{

    @Autowired
    CartMapper cartMapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderItemMapper orderItemMapper;

    @Autowired
    ShippingMapper shippingMapper;

    @Override
    public ServerResponse create(Integer userId, Integer shippingId) {

        //step1:参数非空校验
        if (shippingId==null||shippingId.equals("")){
            return ServerResponse.createServerResponseByFail("收货地址不能为空！");
        }
        //step2:查询购物车已购商品
        List<Cart> cartList = cartMapper.findCartListByUserIdAndChecked(userId);
        //step3:购物车到订单
        ServerResponse serverResponse = getCartOrderItem(userId,cartList);
        if (!serverResponse.isSucess()){
            return serverResponse;
        }
        //step4:创建订单保存到数据库
        //计算订单价格
        BigDecimal orderTotalPrice = new BigDecimal("0");
        List<OrderItem> orderItemList = (List<OrderItem>)serverResponse.getData();
        if (orderItemList==null||orderItemList.size()==0){
            return ServerResponse.createServerResponseByFail("购物车为空！");
        }
        orderTotalPrice = getOrderPrice(orderItemList);
        Order order = createOrder(userId,shippingId,orderTotalPrice);
        if (order==null){
            return ServerResponse.createServerResponseByFail("订单创建失败");
        }
        //step5:订单明细保存到数据库
        for (OrderItem orderItem:orderItemList){
            orderItem.setOrderNo(order.getOrderNo());
        }
        //批量插入
        orderItemMapper.insertBatch(orderItemList);
        //step6:扣库存
        reduceProductStock(orderItemList);
        //step7:清空已下单的购物车
        cleanCart(cartList);
        //step8:返回结果
        OrderVO orderVO = assembleOrderVO(order,orderItemList,shippingId);
        return ServerResponse.createServerResponseBySucess(orderVO);
    }

    private OrderVO assembleOrderVO(Order order,List<OrderItem> orderItemList,Integer shippingId){
        OrderVO orderVO = new OrderVO();

        List<OrderItemVO> orderItemVOList = Lists.newArrayList();
        for (OrderItem orderItem:orderItemList){
            OrderItemVO orderItemVO = assembleOrderItemVO(orderItem);
            orderItemVOList.add(orderItemVO);
        }
        orderVO.setOrderItemVoList(orderItemVOList);
        orderVO.setImageHost(PropertiesUtils.readByKey("imageHost"));
        Shipping shipping = shippingMapper.selectByPrimaryKey(shippingId);
        if(shipping!=null){
            orderVO.setShippingId(shippingId);
            ShippingVO shippingVO = assembleShippingVO(shipping);
            orderVO.setShippingVo(shippingVO);
            orderVO.setReceiverName(shipping.getReceiverName());
        }
        orderVO.setStatus(order.getStatus());
        Const.OrderStatusEunm orderStatusEunm = Const.OrderStatusEunm.codeOf(order.getStatus());
        if (orderStatusEunm!=null){
            orderVO.setStatusDesc(orderStatusEunm.getDesc());
        }
        orderVO.setPostage(0);
        orderVO.setPayment(order.getPayment());
        orderVO.setPaymentType(order.getPaymentType());
        Const.PaymentEunm paymentEunm = Const.PaymentEunm.codeOf(order.getPaymentType());
        if (paymentEunm!=null){
            orderVO.setPaymentTypeDesc(paymentEunm.getDesc());
        }
        orderVO.setOrderNo(order.getOrderNo());
        return orderVO;
    }

    private ShippingVO assembleShippingVO(Shipping shipping){
        ShippingVO shippingVO = new ShippingVO();
        if (shipping!=null){
            shippingVO.setReceiverAddress(shipping.getReceiverAddress());
            shippingVO.setReceiverCity(shipping.getReceiverCity());
            shippingVO.setReceiverDistrict(shipping.getReceiverDistrict());
            shippingVO.setReceiverMobile(shipping.getReceiverMobile());
            shippingVO.setReceiverName(shipping.getReceiverName());
            shippingVO.setReceiverPhone(shipping.getReceiverPhone());
            shippingVO.setReceiverProvince(shipping.getReceiverProvince());
            shippingVO.setReceiverZip(shipping.getReceiverZip());
        }
        return shippingVO;
    }

    private OrderItemVO assembleOrderItemVO(OrderItem orderItem){
        OrderItemVO orderItemVO = new OrderItemVO();
        if (orderItemVO!=null){
            orderItemVO.setOrderNo(orderItem.getOrderNo());
            orderItemVO.setCreateTime(DateUtils.dateToString(orderItem.getCreateTime()));
            orderItemVO.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
            orderItemVO.setProductId(orderItem.getProductId());
            orderItemVO.setProductImage(orderItem.getProductImage());
            orderItemVO.setProductName(orderItem.getProductName());
            orderItemVO.setQuantity(orderItem.getQuantity());
            orderItemVO.setTotalPrice(orderItem.getTotalPrice());
        }
        return orderItemVO;
    }

    /**
     * 清空购物车中已选中的商品
     */
    private void cleanCart(List<Cart> cartList){
        if (cartList!=null&&cartList.size()>0){
            cartMapper.batchDelete(cartList);
        }
    }

    /**
     * 扣库存
     */
    private void  reduceProductStock(List<OrderItem> orderItemList){
        if (orderItemList!=null&&orderItemList.size()>0){
            for (OrderItem orderItem:orderItemList){
                Integer productId = orderItem.getProductId();
                Integer quantity = orderItem.getQuantity();
                Product product = productMapper.selectByPrimaryKey(productId);
                product.setStock(product.getStock()-quantity);
                productMapper.updateByPrimaryKey(product);
            }
        }
    }

    /**
     * 计算订单价格
     */
    private BigDecimal getOrderPrice(List<OrderItem> orderItemList){
        BigDecimal bigDecimal= new BigDecimal("0");
        for (OrderItem orderItem:orderItemList){
            bigDecimal = BigDecinalUtils.add(bigDecimal.doubleValue(),orderItem.getTotalPrice().doubleValue());
        }
        return bigDecimal;
    }

    /**
     * 创建订单
     */
    private Order createOrder(Integer userId, Integer shippingId, BigDecimal orderTotalPrice){
        Order order = new Order();
        order.setOrderNo(generateOrderNO());
        order.setUserId(userId);
        order.setShippingId(shippingId);
        order.setStatus(Const.OrderStatusEunm.ORDER_UNPAY.getCode());
        //订单金额
        order.setPayment(orderTotalPrice);
        order.setPostage(0);
        order.setPaymentType(Const.PaymentEunm.PAYMENT_ONLINE.getCode());

        //保存订单
        int result = orderMapper.insert(order);
        if (result>0){
            return order;
        }
        return null;
    }

    /**
     *生成订单号
     */
    private Long generateOrderNO(){
        return System.currentTimeMillis()+new Random().nextInt(100);
    }

    private ServerResponse getCartOrderItem(Integer userId,List<Cart> cartList){
        if (cartList==null||cartList.size()==0){
            return ServerResponse.createServerResponseByFail("购物车为空！");
        }
        List<OrderItem> orderItemList = Lists.newArrayList();
        for (Cart cart:cartList){
            OrderItem orderItem = new OrderItem();
            orderItem.setUserId(userId);
            Product product = productMapper.selectByPrimaryKey(cart.getProductId());
            if(product==null){
                return ServerResponse.createServerResponseByFail("id为"+cart.getProductId()+"的商品不存在！");
            }
            if(product.getStatus()!= Const.ProductStatusEunm.PRODUCT_ONLINE.getCode()){
                return ServerResponse.createServerResponseByFail("id为"+cart.getProductId()+"的商品未上架或已删除！");
            }
            if (product.getStock()<cart.getQuantity()){
                return ServerResponse.createServerResponseByFail("id为"+cart.getProductId()+"的商品库存不足！");
            }
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setProductId(product.getId());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setProductName(product.getName());
            orderItem.setTotalPrice(BigDecinalUtils.mul(product.getPrice().doubleValue(),cart.getQuantity().doubleValue()));
            orderItemList.add(orderItem);
        }
        return ServerResponse.createServerResponseBySucess(orderItemList);
    }

    @Override
    public ServerResponse get_order_cart_product(Integer userId) {

        OrderCartProductVO orderCartProductVO = new OrderCartProductVO();
        //step1:查询购物车已选中商品
        List<Cart> cartList = cartMapper.findCartListByUserIdAndChecked(userId);
        //step2:购物车到订单
        ServerResponse serverResponse = getCartOrderItem(userId,cartList);
        if (!serverResponse.isSucess()){
            return serverResponse;
        }
        //计算订单价格
        BigDecimal orderTotalPrice = new BigDecimal("0");
        List<OrderItem> orderItemList = (List<OrderItem>)serverResponse.getData();
        if (orderItemList==null||orderItemList.size()==0){
            return ServerResponse.createServerResponseByFail("购物车为空！");
        }
        orderTotalPrice = getOrderPrice(orderItemList);
        orderCartProductVO.setProductTotalPrice(orderTotalPrice);
        orderCartProductVO.setImageHost(PropertiesUtils.readByKey("imageHost"));

        List<OrderItemVO> orderItemVOList = Lists.newArrayList();
        for (OrderItem orderItem:orderItemList){
            OrderItemVO orderItemVO = assembleOrderItemVO(orderItem);
            orderItemVOList.add(orderItemVO);
        }
        orderCartProductVO.setOrderItemVoList(orderItemVOList);

        return ServerResponse.createServerResponseBySucess(orderCartProductVO);
    }

    @Override
    public ServerResponse list(Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Order>orderList = Lists.newArrayList();
        if (userId==null){//管理员
            orderList = orderMapper.selectAll();
        }else{
            orderList = orderMapper.findOrderListByUserId(userId);

        }
        if (orderList==null||orderList.size()==0){
            return ServerResponse.createServerResponseByFail("未查询到订单信息！");
        }
        List<OrderVO>orderVOList = Lists.newArrayList();
        for (Order order:orderList){
            List<OrderItem>orderItemList = orderItemMapper.findOrderItemListByUserIdAndOrderNO(userId,order.getOrderNo());
            /*if (orderItemList==null||orderItemList.size()==0){
                return ServerResponse.createServerResponseByFail("未查询到订单信息！");
            }*/
            OrderVO orderVO = assembleOrderVO(order,orderItemList,order.getShippingId());
            orderVOList.add(orderVO);
        }
        //返回结果
        PageInfo pageInfo = new PageInfo(orderList);
        pageInfo.setList(orderVOList);
        return ServerResponse.createServerResponseBySucess(pageInfo);
    }

    @Override
    public ServerResponse detail(Integer userId, Long orderNo) {
        if (orderNo==null||orderNo.equals("")){
            return ServerResponse.createServerResponseByFail("查询的订单号不能为空！");
        }
        Order order = orderMapper.findOrderListByUserIdAndOrderNO(userId,orderNo);
        if (order==null){
            return ServerResponse.createServerResponseByFail("未查询到订单信息！");
        }
        List<OrderItem>orderItemList = orderItemMapper.findOrderItemListByUserIdAndOrderNO(userId,orderNo);
        if (orderItemList==null||orderItemList.size()==0){
            return ServerResponse.createServerResponseByFail("未查询到订单信息！");
        }
        OrderVO orderVO = assembleOrderVO(order,orderItemList,order.getShippingId());
        //返回结果
        return ServerResponse.createServerResponseBySucess(orderVO);
    }

    @Override
    public ServerResponse cancel(Integer userId, Long orderNo) {
        if (orderNo==null||orderNo.equals("")){
            return ServerResponse.createServerResponseByFail("查询的订单号不能为空！");
        }
        Order order = orderMapper.findOrderListByUserIdAndOrderNO(userId,orderNo);
        if (order==null){
            return ServerResponse.createServerResponseByFail("未查询到订单信息！");
        }
        if (order.getStatus()!=Const.OrderStatusEunm.ORDER_UNPAY.getCode()){
            return ServerResponse.createServerResponseByFail("订单不可取消！");
        }
        order.setStatus(Const.OrderStatusEunm.ORDER_CANCELED.getCode());
        int result = orderMapper.updateByPrimaryKey(order);
        if (result>0){
            return ServerResponse.createServerResponseBySucess("订单取消成功!");
        }
        return ServerResponse.createServerResponseByFail("订单取消失败!");
    }
}
