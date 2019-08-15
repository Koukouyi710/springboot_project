package com.neuedu.service;

import com.neuedu.common.ServerResponse;

public interface ICategoryService {

    /**
     * 获取品类的子节点（平级）
     */
    public ServerResponse get_category(Integer categoryId);

}
