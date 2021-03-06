package com.xuecheng.filesystem.rabbitmq.mq;

import com.rabbitmq.client.Channel;
import com.xuecheng.filesystem.rabbitmq.config.RabbitmqConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ReceiveHandler {

    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_EMAIL})
    public void send_email(String msg, Message message, Channel channel){
        System.out.println(msg);
    }

    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_SMS})
    public void send_sms(String msg, Message message, Channel channel){
        System.out.println(msg);
    }
}
