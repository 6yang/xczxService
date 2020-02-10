package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.client.FileSystemClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class testFign {

    @Autowired
    FileSystemClient fileSystemClient;

    @Test
    public void testDELTE() {
        ResponseResult delete = fileSystemClient.delete("group1/M00/00/00/rBCpbF4_8GGAJ4OOAAhA6ZCgPdw680.png");
        System.out.println(delete);
    }
}
