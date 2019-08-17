package com.neuedu.service.impl;

import com.google.common.collect.Lists;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CartMapper;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Cart;
import com.neuedu.pojo.Product;
import com.neuedu.service.ICartService;
import com.neuedu.utils.BigDecinalUtils;
import com.neuedu.vo.CartProductVO;
import com.neuedu.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartServiceImpl implements ICartService{

    @Autowired
    CartMapper cartMapper;

    @Autowired
    ProductMapper productMapper;

    @Override
    public ServerResponse list(Integer userId) {
        CartVO cartVO = assembleCartVO(userId);
        return ServerResponse.createServerResponseBySucess(cartVO);
    }

    @Override
    public ServerResponse add(Integer userId,Integer productId, Integer count) {

        //Step1:参数非空校验
        if (productId==null||count==null){
            return ServerResponse.createServerResponseByFail("参数不能为空！");
        }
        //Step2:根据参数查询信息
        Cart cart =cartMapper.selectCartByUserIdAndProductId(userId,productId);
        if (cart==null){
            //添加
            Cart cart_add = new Cart();
            cart_add.setProductId(productId);
            cart_add.setUserId(userId);
            cart_add.setQuantity(count);
            cart_add.setChecked(Const.CartStatusEunm.PRODUCT_CHECKED.getCode());
            int result = cartMapper.insert(cart_add);
            if (result<=0){
                return ServerResponse.createServerResponseByFail("更新数据失败!");
            }
        }else{
            //更新
            Cart cart_update = new Cart();
            cart_update.setId(cart.getId());
            cart_update.setProductId(productId);
            cart_update.setUserId(userId);
            cart_update.setQuantity(count);
            cart_update.setChecked(Const.CartStatusEunm.PRODUCT_CHECKED.getCode());
            int result = cartMapper.updateByPrimaryKey(cart_update);
            if (result<=0){
                return ServerResponse.createServerResponseByFail("更新数据失败!");
            }
        }
        //Step3:返回结果
        CartVO cartVO = assembleCartVO(userId);
        return ServerResponse.createServerResponseBySucess(cartVO);
    }

    private CartVO assembleCartVO(Integer userId){
        CartVO cartVO = new CartVO();
        //Step1:根据id查询
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        //Step2:转换VO
        List<CartProductVO> cartProductVOList = Lists.newArrayList();
        //购物车总价
        BigDecimal carttotalprice = new BigDecimal("0.00");
        if (cartList!=null&&cartList.size()>0){
            for (Cart cart:cartList){
                CartProductVO cartProductVO = new CartProductVO();
                cartProductVO.setId(cart.getId());
                cartProductVO.setQuantity(cart.getQuantity());
                cartProductVO.setUserId(cart.getUserId());
                cartProductVO.setProductChecked(cart.getChecked());
                //查询商品
                Product product = productMapper.selectByPrimaryKey(cart.getProductId());
                if (product!=null){
                    cartProductVO.setProductId(cart.getProductId());
                    cartProductVO.setProductMainImage(product.getMainImage());
                    cartProductVO.setProductName(product.getName());
                    cartProductVO.setProductPrice(product.getPrice());
                    cartProductVO.setProductStatus(product.getStatus());
                    cartProductVO.setProductStock(product.getStock());
                    cartProductVO.setProductSubtitle(product.getSubtitle());
                    int stock = product.getStock();
                    int limitProductCount = 0;
                    if (stock>=cart.getQuantity()){
                        limitProductCount = cart.getQuantity();
                        cartProductVO.setLimitQuantity("LIMIT_NUM_SUCCESS");
                    }else{//库存不足
                        limitProductCount = stock;
                        //更新信息
                        Cart cart_update = new Cart();
                        cart_update.setId(cart.getId());
                        cart_update.setQuantity(stock);
                        cartMapper.updateByPrimaryKey(cart_update);
                        cartProductVO.setLimitQuantity("LIMIT_NUM_FAIL");
                    }
                    cartProductVO.setQuantity(limitProductCount);
                    cartProductVO.setProductTotalPrice(BigDecinalUtils.mul(product.getPrice().doubleValue(),Double.valueOf(cartProductVO.getQuantity())));
                }

                //Step3:计算总价格
                carttotalprice = BigDecinalUtils.add(carttotalprice.doubleValue(),cartProductVO.getProductTotalPrice().doubleValue());

                cartProductVOList.add(cartProductVO);
            }
        }
        cartVO.setCartProductVOList(cartProductVOList);
        cartVO.setCarttotalprice(carttotalprice);

        //Step4:判断全选
        int count = cartMapper.isCheckedAll(userId);
        if (count>0){//未全选
            cartVO.setIsallchecked(false);
        }else{
            cartVO.setIsallchecked(true);
        }
        //Step5:返回
        return cartVO;
    }


    @Override
    public ServerResponse update(Integer userId, Integer productId, Integer count) {

        //Step1:参数非空校验
        if (productId==null||count==null){
            return ServerResponse.createServerResponseByFail("参数不能为空！");
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product==null){
            return ServerResponse.createServerResponseByFail("商品不存在");
        }
        //Step2:根据参数查询信息
        Cart cart =cartMapper.selectCartByUserIdAndProductId(userId,productId);
        if (cart!=null){
            cart.setQuantity(count);
            int result = cartMapper.updateByPrimaryKey(cart);
            if (result<=0){
                return ServerResponse.createServerResponseByFail("更新数据失败!");
            }
        }else{
            return ServerResponse.createServerResponseByFail("更新数据失败!");
        }
        //Step3:返回结果
        CartVO cartVO = assembleCartVO(userId);
        return ServerResponse.createServerResponseBySucess(cartVO);
    }

    @Override
    public ServerResponse delete_product(Integer userId, String productIds) {
        //Step1:参数非空校验
        if (productIds==null||productIds.equals("")){
            return ServerResponse.createServerResponseByFail("参数不能为空！");
        }
        //Step2:根据参数查询信息
       List<Integer> productIdList = Lists.newArrayList();
        String[] products = productIds.split(",");
        if (products!=null&&products.length>0){
            for (String productArr:products){
                Integer productId = Integer.parseInt(productArr);
                productIdList.add(productId);
            }
        }
        //Step3:删除
        int result = cartMapper.deleteByUserIdAndProductIdList(userId,productIdList);
        if (result<=0){
            return ServerResponse.createServerResponseByFail("商品不存在！");
        }
        //Step4:返回结果
        CartVO cartVO = assembleCartVO(userId);
        return ServerResponse.createServerResponseBySucess(cartVO);
    }

    @Override
    public ServerResponse select(Integer userId, Integer productId,Integer check) {
        //Step1:参数非空校验
        if (productId==null){
            return ServerResponse.createServerResponseByFail("参数不能为空！");
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product==null){
            return ServerResponse.createServerResponseByFail("商品不存在!");
        }
        //Step2:根据参数查询信息
        int result = cartMapper.selectOrUnselectProduct(userId,productId,check);
        if(result<=0){
            return ServerResponse.createServerResponseByFail("商品不存在！");
        }
        //Step3:返回结果
        CartVO cartVO = assembleCartVO(userId);
        return ServerResponse.createServerResponseBySucess(cartVO);
    }

    @Override
    public ServerResponse select_all(Integer userId, Integer check) {

        int result = cartMapper.selectOrUnselectProduct(userId,null,check);
        if(result<=0){
            return ServerResponse.createServerResponseByFail("商品不存在！");
        }
        //返回结果
        CartVO cartVO = assembleCartVO(userId);
        return ServerResponse.createServerResponseBySucess(cartVO);
    }

    @Override
    public ServerResponse get_cart_product_count(Integer userId) {
        int count = cartMapper.getCartProductCount(userId);
        return ServerResponse.createServerResponseBySucess(count);
    }

}
