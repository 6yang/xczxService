package com.xuecheng.filesystem.rabbitmq;

import com.xuecheng.filesystem.rabbitmq.config.RabbitmqConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Producer05_topics_springboot {


    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void testSendEmail() {

        String message = "send email and sms ";
        /**
        * 参数
         * 1、交换机名称
         * 2、routingkey
         * 3、消息内容
        * */
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_TOPIC_INFORM,"inform.sms.email",message);
    }
}
