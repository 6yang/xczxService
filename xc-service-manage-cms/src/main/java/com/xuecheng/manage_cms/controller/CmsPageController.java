package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.CmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {

    @Autowired
    CmsPageService pageService;

    /**
     * 分页查询页面列表
     * @param page
     * @param size
     * @param queryPageRequest
     * @return : com.xuecheng.framework.model.response.QueryResponseResult
     */
    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findCmsPageList(@PathVariable("page") int page, @PathVariable("size") int size, QueryPageRequest queryPageRequest) {
        return pageService.findCmsPageList(page,size,queryPageRequest);
    }
    /**
     * 新增页面
     * @param cmsPage
     * @return : com.xuecheng.framework.domain.cms.response.CmsPageResult
     */
    @Override
    @PostMapping("/add")
    public CmsPageResult addCmsPage(@RequestBody CmsPage cmsPage) {
        return pageService.addCmsPage(cmsPage);
    }

    /**
     * 根据id查询页面信息
     * @param id
     * @return : com.xuecheng.framework.domain.cms.CmsPage
     */
    @Override
    @GetMapping("/get/{id}")
    public CmsPage findCmsPageById(@PathVariable("id") String id) {
        return pageService.findCmsPageById(id);
    }

    /**
     * 修改页面信息
     * @param id
     * @param cmsPage
     * @return : com.xuecheng.framework.domain.cms.response.CmsPageResult
     */
    @Override
    @PutMapping("/edit/{id}")
    public CmsPageResult editCmsPage(@PathVariable("id") String id, @RequestBody CmsPage cmsPage) {
        return pageService.editCmsPage(id,cmsPage);
    }

    /**
     * 删除页面
     * @param id
     * @return : com.xuecheng.framework.model.response.ResponseResult
     */
    @Override
    @DeleteMapping("/del/{id}")
    public ResponseResult deleteCmsPage(@PathVariable("id") String id) {
        return pageService.deleteCmsPage(id);
    }
}
