package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CategoryControllerApi;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController implements CategoryControllerApi {

    @Autowired
    private CategoryService categoryService;

    /**
     * 查询分类列表
     * @param
     * @return : com.xuecheng.framework.domain.course.ext.CategoryNode
     */
    @Override
    @GetMapping("/list")
    public CategoryNode findCategoryList() {
        return categoryService.selectCategoryList();
    }
}
