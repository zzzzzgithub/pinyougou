package com.itheima.listener;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.itheima.utils.SmsUtils;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author pdd
 * @version 1.0
 * @date 2019/7/28 20:28
 */
@Component
public class SmsMessageListener implements MessageListenerConcurrently {
    @Autowired
    private SmsUtils smsUtils;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        //循环读取消息
        try {
            for (MessageExt msg : msgs) {
                //读取内容
                String body = new String(msg.getBody(), RemotingHelper.DEFAULT_CHARSET);
                //读取参数列表
                Map<String, String> map = JSON.parseObject(body, Map.class);
                //调用工具发送消息
                SendSmsResponse response = smsUtils.sendSms(
                        map.get("mobile"),
                        map.get("template_code"),
                        map.get("sign_name"),
                        map.get("param")
                );
                //输出发送结果
                System.out.println("短信接口返回数据--------");
                System.out.println("Code=" + response.getCode());
                System.out.println("Message=" + response.getMessage());
                System.out.println("RequestId=" + response.getRequestId());
                System.out.println("BizId=" + response.getBizId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回消息状态
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
