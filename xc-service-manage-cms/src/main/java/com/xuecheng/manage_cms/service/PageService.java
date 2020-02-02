package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PageService {

    @Autowired
    CmsPageRepository cmsPageRepository;

   /**
    * 分页查询所有页面
    * @param page
    * @param size
    * @param queryPageRequest
    * @return : com.xuecheng.framework.model.response.QueryResponseResult
    */
    public QueryResponseResult findCmsPageList(int page, int size, QueryPageRequest queryPageRequest){
        if(queryPageRequest==null){
            queryPageRequest = new QueryPageRequest();
        }
        //自定义条件查询
        //定义条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        CmsPage cms = new CmsPage();
        //设置条件值
        if(StringUtils.isNotEmpty(queryPageRequest.getSiteId())){
            cms.setSiteId(queryPageRequest.getSiteId());
        }
        if(StringUtils.isNotEmpty(queryPageRequest.getTemplateId())){
            cms.setTemplateId(queryPageRequest.getTemplateId());
        }
        if(StringUtils.isNotEmpty(queryPageRequest.getPageAliase())){
            cms.setPageAliase(queryPageRequest.getPageAliase());
        }
        //定义条件对象Example
        Example<CmsPage> example = Example.of(cms,exampleMatcher);

        //分页参数
        if(page<=0){
            page= 1;
        }
        page = page -1;
        if (size <= 0) {
            size =10;
        }
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> cmsPage = cmsPageRepository.findAll(example,pageable);
        QueryResult<CmsPage> queryResult = new QueryResult();
        queryResult.setList(cmsPage.getContent());
        queryResult.setTotal(cmsPage.getTotalElements());
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }

    /**
     * 新增页面
     * @param cmsPage
     * @return : com.xuecheng.framework.domain.cms.response.CmsPageResult
     */
    public CmsPageResult addCmsPage(CmsPage cmsPage){
        //校验页面名称、站点ID、页面webpath唯一性
        //根据页面名称、站点ID、页面webpath去cms_page集合，如果查到说明此页面已经存在，若查询不到继续添加
        CmsPage find_cmsPage = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if(find_cmsPage == null){
            cmsPage.setPageId(null);
            //调用dao新增页面
            cmsPageRepository.save(cmsPage);
            return new CmsPageResult(CommonCode.SUCCESS,cmsPage);
        }
        //提交添加失败
        return new CmsPageResult(CommonCode.FAIL,null);
    }

    /**
     * 根据id查询页面信息
     * @return : com.xuecheng.framework.domain.cms.CmsPage
     * @param id
     */
    public CmsPage findCmsPageById(String id){
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if(optional.isPresent()){
            CmsPage cmsPage = optional.get();
            return cmsPage;
        }
        return null;
    }

    /**
     * 修改cmspage信息
     * @param id
     * @param cmsPage
     * @return : com.xuecheng.framework.domain.cms.response.CmsPageResult
     */
    public CmsPageResult editCmsPage(String id,CmsPage cmsPage){
        CmsPage one = findCmsPageById(id);
        if(one != null ){
            //更新模板id
            one.setTemplateId(cmsPage.getTemplateId());
            //更新所属站点
            one.setSiteId(cmsPage.getSiteId());
            //更新页面别名
            one.setPageAliase(cmsPage.getPageAliase());
            //更新页面名称
            one.setPageName(cmsPage.getPageName());
            //更新访问路径
            one.setPageWebPath(cmsPage.getPageWebPath());
            //更新物理路径
            one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());

            CmsPage save = cmsPageRepository.save(one);
            if(save!=null){
                return new CmsPageResult(CommonCode.SUCCESS,save);
            }
        }
        //修改失败
        return new CmsPageResult(CommonCode.FAIL,null);
    }

    /**
     * 删除页面
     * @param id
     * @return : com.xuecheng.framework.model.response.ResponseResult
     */
    public ResponseResult deleteCmsPage(String id){
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if(optional.isPresent()){
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

}
