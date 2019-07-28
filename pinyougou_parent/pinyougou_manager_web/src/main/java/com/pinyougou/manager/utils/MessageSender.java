package com.pinyougou.manager.utils;

import com.alibaba.fastjson.JSON;
import entity.MessageInfo;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;


/**
 * @author pdd
 * @version 1.0
 * @date 2019/7/27 16:46
 */
@Component
public class MessageSender {
    @Autowired
    private DefaultMQProducer producer;

    public void sendMessage(MessageInfo info) {
        //把消息对象转换成json串发送内容
        String content = JSON.toJSONString(info);
        try {
            Message message = new Message(
                    info.getTopic(),//主题名称
                    info.getTags(),//标签名称
                    info.getKeys(),//key
                    content.getBytes(RemotingHelper.DEFAULT_CHARSET)//发送的内容
            );
            producer.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
