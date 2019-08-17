package com.neuedu.service.impl;

import com.google.common.collect.Sets;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.pojo.Category;
import com.neuedu.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements ICategoryService{

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public ServerResponse get_category(Integer categoryId) {
        //step1:非空校验
        if (categoryId==null||categoryId.equals("")){
            return ServerResponse.createServerResponseByFail("参数不能为空！");
        }
        //step2:根据id查类别
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category==null){
            return ServerResponse.createServerResponseByFail("所查询类别不存在！");
        }
        //step3:查询子类别
        List<Category> categoryList = categoryMapper.findChildCategory(categoryId);
        //step4:返回结果

        return ServerResponse.createServerResponseBySucess(categoryList);
    }

    @Override
    public ServerResponse add_category(Category category) {
        //step1:非空校验
        if (category.getName()==null||category.getName().equals("")){
            return ServerResponse.createServerResponseByFail("类别名称不能为空！");
        }
        if (category.getParentId()==null||category.getParentId().equals("")){
            category.setParentId(0);
        }
        category.setStatus(1);
        //step2:添加到数据库
        int result = categoryMapper.insert(category);
        //step3:返回结果
        if (result>0){
            return ServerResponse.createServerResponseBySucess("添加类别成功！");
        }
        return ServerResponse.createServerResponseByFail("添加类别失败！");
    }

    @Override
    public ServerResponse set_category(Category category) {
        //step1:非空校验
        if (category.getId()==null||category.getId().equals("")){
            return ServerResponse.createServerResponseByFail("类别id不能为空！");
        }
        if (category.getParentId()==null||category.getParentId().equals("")){
            category.setParentId(0);
        }
        category.setStatus(1);
        //step2:添加到数据库
        Category category_res = categoryMapper.selectByPrimaryKey(category.getId());
        if (category_res==null){
            return ServerResponse.createServerResponseByFail("所修改类别不存在或已删除！");
        }
        int result = categoryMapper.updateByPrimaryKey(category);
        //step3:返回结果
        if (result>0){
            return ServerResponse.createServerResponseBySucess("修改类别成功！");
        }
        return ServerResponse.createServerResponseByFail("修改类别失败！");
    }

    @Override
    public ServerResponse get_deep_category(Integer categoryId) {
        //step1:参数的非空校验
        if (categoryId==null||categoryId.equals("")){
            return ServerResponse.createServerResponseByFail("类别id不能为空！");
        }
        //step2:递归查询
        Set<Category> categorySet = Sets.newHashSet();
        categorySet = findAllChildCategory(categorySet,categoryId);

        Set<Integer> integerSet = Sets.newHashSet();

        Iterator<Category> categoryIterator = categorySet.iterator();
        while (categoryIterator.hasNext()){
            Category category = categoryIterator.next();
            integerSet.add(category.getId());
        }
        return ServerResponse.createServerResponseBySucess(integerSet);
    }

    private Set<Category> findAllChildCategory(Set<Category> categorySet,Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category!=null){
            categorySet.add(category);
        }
        //查找categoryId下的子节点（平级）
        List<Category> categoryList = categoryMapper.findChildCategory(categoryId);
        if (categoryList!=null&&categoryList.size()>0){
            for(Category category1:categoryList){
                findAllChildCategory(categorySet,category1.getId());
            }
        }
        return categorySet;
    }
}
