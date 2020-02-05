package com.xuecheng.manage_cms_client.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.manage_cms_client.service.CmsPageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/*
* 监听mq 接收页面发布的消息
* */
@Component
public class ConsumerPostPage {

    @Autowired
    private CmsPageService cmsPageService;

    @RabbitListener(queues = {"${xuecheng.mq.queue}"})
    public void postPage(String msg){
        //解析消息
        Map map = JSON.parseObject(msg, Map.class);
        //得到消息中的pageid
        String pageId = (String) map.get("pageId");
        //调用service将GridFs中页面下载到本地
        cmsPageService.savePageToServerPath(pageId);
    }
}
