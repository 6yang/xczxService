package com.xuecheng.manage_cms.controller;

import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms.service.CmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

@Controller
public class CmsPagePreviewController extends BaseController {

    @Autowired
    private CmsPageService cmsPageService;

    @RequestMapping(value="/cms/preview/{pageId}",method = RequestMethod.GET)
    public void pagePreview(@PathVariable("pageId") String pageId) throws IOException {
        //执行静态化
        String pageHtml = cmsPageService.getPageHtml(pageId);
        ServletOutputStream os = response.getOutputStream();
        os.write(pageHtml.getBytes("utf-8"));
    }

}
