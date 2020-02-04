package com.xuecheng.manage_cms.dao;

import com.xuecheng.manage_cms.service.CmsPageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PageServiceTest {

    @Autowired
    private CmsPageService cmsPageService;

    @Test
    public void testGenerateHtml() {
        String pageHtml = cmsPageService.getPageHtml("5e38d0314107801240e6eb2a");
        System.out.println(pageHtml);
    }
}

