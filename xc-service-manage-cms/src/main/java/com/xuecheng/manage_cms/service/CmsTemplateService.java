package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CmsTemplateService {

    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;

    /**
     * 查询模板列表
     * @param
     * @return : com.xuecheng.framework.model.response.QueryResponseResult
     */
    public QueryResponseResult findCmsTemplateList() {
        List<CmsTemplate> cmsTemplateList = cmsTemplateRepository.findAll();
        QueryResult<CmsTemplate> queryResult = new QueryResult<>();
        queryResult.setList(cmsTemplateList);
        queryResult.setTotal(cmsTemplateList.size());
        return new QueryResponseResult(CommonCode.SUCCESS,queryResult);
    }
}
