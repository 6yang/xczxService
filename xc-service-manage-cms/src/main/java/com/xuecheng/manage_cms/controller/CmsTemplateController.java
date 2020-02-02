package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsTemplateControllerApi;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.manage_cms.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cms/template")
public class CmsTemplateController implements CmsTemplateControllerApi {

    @Autowired
    private TemplateService templateService;

    /**
     * 查询模板列表
     * @param
     * @return : com.xuecheng.framework.model.response.QueryResponseResult
     */
    @Override
    @GetMapping("/list")
    public QueryResponseResult findCmsTemplateList() {
        return templateService.findCmsTemplateList();
    }
}
