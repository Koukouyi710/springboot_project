package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;
import org.springframework.web.multipart.MultipartFile;

public interface IProductService {

    /**
     *新增or更新商品
     */
    public ServerResponse save(Product product);

    /**
     *商品上下架
     */
    public ServerResponse set_sale_status(Integer productId,Integer status);

    /**
     *后台-商品详情
     */
    public ServerResponse detail(Integer productId);

    /**
     *商品列表
     */
    public ServerResponse list(Integer pageNum,Integer pageSize);

    /**
     *商品列表
     */
    public ServerResponse ulist(Integer categoryId,String keyword,Integer pageNum,Integer pageSize,String orderBy);

    /**
     *商品搜索
     */
    public ServerResponse search(String productName,Integer productId,Integer pageNum,Integer pageSize);

    /**
     * 图片上传
     */
    public ServerResponse upload(MultipartFile file,String path);

    /**
     *前台-商品详情
     */
    public ServerResponse udetail(Integer productId,Integer is_new ,Integer is_hot ,Integer is_banner );

    /**
     *前台-获取商品分类
     */
    public ServerResponse topcategory(Integer sid);

    /**
     *根据Id查询商品
     */
    public ServerResponse findProductById(Integer productId);

}
