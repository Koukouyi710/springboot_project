package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Category;

public interface ICategoryService {

    /**
     * 获取品类的子节点（平级）
     */
    public ServerResponse get_category(Integer categoryId);

    /**
     * 增加节点
     */
    public ServerResponse add_category(Category category);

    /**
     * 修改节点
     */
    public ServerResponse set_category(Category category);

    /**
     * 获取当前分类id及递归子节点categoryId
     */
    public ServerResponse get_deep_category(Integer categoryId);

}
