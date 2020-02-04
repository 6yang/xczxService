package com.xuecheng.manage_cms.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
public class CmsPageService {

    @Autowired
    CmsPageRepository cmsPageRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CmsTemplateRepository cmsTemplateRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    GridFSBucket gridFSBucket;

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
        if(find_cmsPage != null){
            //页面名称已经存在
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        cmsPage.setPageId(null);
        //调用dao新增页面
        cmsPageRepository.save(cmsPage);
        return new CmsPageResult(CommonCode.SUCCESS,cmsPage);
        //提交添加失败

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
            //更新数据Url,
            one.setDataUrl(cmsPage.getDataUrl());
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

    /**
     * 页面静态化方法
     * @param pageId
     * @return : java.lang.String
     */
    public String getPageHtml(String pageId){
        /*
        * 静态化程序获取页面的DataUrl
          静态化程序远程请求DataUrl获取数据模型。
          静态化程序获取页面的模板信息
          执行页面静态化
        * */
        Map model = this.getModelByPageId(pageId);
        if(model==null){
            //数据模型获取不到
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        String template = this.getTemplateByPageId(pageId);
        if(StringUtils.isEmpty(template)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //执行静态化
        String html = this.generateHtml(template, model);
        return html;
    }


    /**
     * 获取数据模型
     * @param pageId
     * @return : java.util.Map
     */
    private Map getModelByPageId(String pageId){
        //取出页面的信息
        CmsPage cmsPage = this.findCmsPageById(pageId);
        if(cmsPage==null){
            //页面不存在
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        //取出页面的dataUrl
        if(StringUtils.isEmpty(cmsPage.getDataUrl())){
            //页面的dataUrl为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(cmsPage.getDataUrl(), Map.class);
        Map body = forEntity.getBody();
        return body;
    }

    /**
     * 获取页面的模板信息
     * @param pageId
     * @return : java.lang.String
     */
    public String getTemplateByPageId(String pageId){
        //取出页面的信息
        CmsPage cmsPage = this.findCmsPageById(pageId);
        if(cmsPage==null){
            //页面不存在
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        String templateId = cmsPage.getTemplateId();
        if(StringUtils.isEmpty(templateId)){
            //页面模板为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        Optional<CmsTemplate> optionalTemplate = cmsTemplateRepository.findById(templateId);
        if(optionalTemplate.isPresent()){
            CmsTemplate cmsTemplate = optionalTemplate.get();
            //取模板文件ID
            String templateFileId = cmsTemplate.getTemplateFileId();
            //从GridFs中取模板文件的内容
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            //打开一个下载流
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            //创建一个GridFsResource 对象，获取流
            GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
            //从流中取数据
            String content = null;
            try {
                content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
                return content;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 执行静态化方法
     * @param template
     * @param model
     * @return : java.lang.String
     */
    public String generateHtml(String templateContent,Map model){
        //创建配置对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        //创建模板加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template",templateContent);
        //在配置中设置模板加载器
        configuration.setTemplateLoader(stringTemplateLoader);
        //获取模板
        try {
            Template template = configuration.getTemplate("template", "utf-8");
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return null;
    }
}
