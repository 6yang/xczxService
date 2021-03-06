package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsSiteControllerApi;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.manage_cms.service.CmsSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cms/site")
public class CmsSiteController implements CmsSiteControllerApi {

    @Autowired
    private CmsSiteService siteService;

    /**
     * 查找所有的站点列表
     * @param
     * @return : com.xuecheng.framework.model.response.QueryResponseResult
     */
    @Override
    @GetMapping("/list")
    public QueryResponseResult findCmsSiteList() {
        return siteService.findCmsSiteList();
    }
}
