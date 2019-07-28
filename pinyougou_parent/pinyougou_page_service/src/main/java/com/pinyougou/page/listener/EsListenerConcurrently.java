package com.pinyougou.page.listener;

import com.alibaba.fastjson.JSON;
import com.pinyougou.page.service.ItemPageService;
import entity.EsItem;
import entity.MessageInfo;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.List;

/**
 * @author pdd
 * @version 1.0
 * @date 2019/7/27 17:24
 */
public class EsListenerConcurrently implements MessageListenerConcurrently {
    @Autowired
    private ItemPageService itemPageService;
    @Value("${PAGEDIR}")
    private String PAGEDIR;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        //循环读取消息
        try {
            for (MessageExt msg : msgs) {
                //输出消息主题,标签,keys,内容
                String body = new String(msg.getBody(), RemotingHelper.DEFAULT_CHARSET);
                //转换为messageinfo
                MessageInfo info = JSON.parseObject(body, MessageInfo.class);
                //新增操作
                if (info.getMethod() == MessageInfo.METHOD_ADD) {
                    //转换对象
                    List<EsItem> esItemList = JSON.parseArray(info.getContext().toString(), EsItem.class);
                    //生成静态页面
                    for (EsItem esItem : esItemList) {
                        itemPageService.genItemHtml(esItem.getGoodsId());
                    }
                } else if (info.getMethod() == MessageInfo.METHOD_DELETE) {
                    List<Long> idArray = JSON.parseArray(info.getContext().toString(), Long.class);
                    //list转数组
                    Long[] ids = new Long[idArray.size()];
                    idArray.toArray(ids);
                    //删除静态详情页
                    for (Long id : ids) {
                        File beDele = new File(PAGEDIR + id + ".html");
                        boolean exists = beDele.exists();
                        if (exists) {
                            boolean result = beDele.delete();
                            System.out.println("删除商品" + id + "静态页:" + result);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回消息读取状态-CONSUME_SUCCESS
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
