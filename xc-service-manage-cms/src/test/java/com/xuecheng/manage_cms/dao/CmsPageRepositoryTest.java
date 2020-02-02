package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {

    @Autowired
    CmsPageRepository cmsPageRepository;

    //测试分页查找
    @Test
    public void testFindAll() {
        Pageable pageable = PageRequest.of(0,10);

        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        for (CmsPage cmsPage : all) {
            System.out.println(cmsPage);
        }
    }

    @Test
    public void testFindAllByExample() {

        Pageable pageable = PageRequest.of(0, 10);
        // 查询站点
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
        cmsPage.setTemplateId("5a962bf8b00ffc514038fafa");
        //设置页面别名模糊查询
        cmsPage.setPageAliase("轮播");
        //条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        exampleMatcher = exampleMatcher.withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());
//        ExampleMatcher.GenericPropertyMatchers.contains() 包含关键字
//        ExampleMatcher.GenericPropertyMatchers.endsWith() 后缀匹配
        //定义example
        Example<CmsPage> example = Example.of(cmsPage,exampleMatcher);
        Page<CmsPage> all = cmsPageRepository.findAll(example,pageable);
        System.out.println(all.getContent());
    }

    //测试删除
    @Test
    public void testInsert() {
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageId("s0001");
        cmsPage.setTemplateId("t01");
        cmsPage.setPageName("测试页面");
        cmsPage.setPageCreateTime(new Date());
        cmsPageRepository.save(cmsPage);
        System.out.println(cmsPage);
    }

    //测试删除
    @Test
    public void testDelete() {
        cmsPageRepository.deleteById("s01");
    }

    @Test
    public void testUpdate() {
        Optional<CmsPage> page = cmsPageRepository.findById("s0001");
        if(page.isPresent()){
            CmsPage cmsPage = page.get();
            cmsPage.setPageName("修改后的测试页面");
            cmsPageRepository.save(cmsPage);
        }
    }

    //根据页面查询

    @Test
    public void testfindBypagename() {
        CmsPage cmsPage = cmsPageRepository.findByPageName("修改后的测试页面");
        System.out.println(cmsPage);
    }
}
