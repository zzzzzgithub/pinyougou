package com.itheima.conf;

import com.itheima.listener.SmsMessageListener;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author pdd
 * @version 1.0
 * @date 2019/7/28 21:05
 */
@Configuration
public class  ConsumerConfiguration {
    @Autowired
    private SmsMessageListener smsMessageListener;

    @Bean
    public DefaultMQPushConsumer getConsumer() {
        DefaultMQPushConsumer consumer = null;
        try {
            //1.创建推动式消费者
            consumer = new DefaultMQPushConsumer("sms-consumer-group");
            //2.设置NameServer
            consumer.setNamesrvAddr("127.0.0.1:9876");
            //3.设置消息的主题
            consumer.subscribe("topic-sms", "*");
            //4.//创建消息监听器
            consumer.setMessageListener(smsMessageListener);
            //5.启动消费者
            consumer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return consumer;
    }
}
