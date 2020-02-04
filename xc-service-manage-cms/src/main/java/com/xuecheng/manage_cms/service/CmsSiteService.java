package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CmsSiteService {

    @Autowired
    private CmsSiteRepository cmsSiteRepository;

    /**
     * 查找所有的站点列表
     * @param
     * @return : com.xuecheng.framework.model.response.QueryResponseResult
     */
    public QueryResponseResult findCmsSiteList() {

        List<CmsSite> cmsSiteList = cmsSiteRepository.findAll();
        QueryResult<CmsSite> queryResult = new QueryResult<>();
        queryResult.setList(cmsSiteList);
        queryResult.setTotal(cmsSiteList.size());
        return  new QueryResponseResult(CommonCode.SUCCESS,queryResult);
    }
}
