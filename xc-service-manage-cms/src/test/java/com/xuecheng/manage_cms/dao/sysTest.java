package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.system.SysDictionary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class sysTest {

    @Autowired
    private SysDictionaryDao sysDictionaryDao;

    @Test
    public void testfind() {
        SysDictionary byDType = sysDictionaryDao.findByDType("200");
        System.out.println(byDType);
    }
}
