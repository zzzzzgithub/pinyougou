package com.pinyougou.search.listener;

import com.alibaba.fastjson.JSON;
import com.pinyougou.search.service.ItemSearchService;
import entity.EsItem;
import entity.MessageInfo;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author pdd
 * @version 1.0
 * @date 2019/7/27 17:04
 */
public class EsListenerConcurrently implements MessageListenerConcurrently {
    @Autowired
    private ItemSearchService itemSearchService;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext consumeConcurrentlyContext) {

        //1.1循环读取消息msgs
        try {
            for (MessageExt msg : msgs) {
                //1.2输出消息
                String body = new String(msg.getBody(), RemotingHelper.DEFAULT_CHARSET);
                //转换为MessageInfo
                MessageInfo info = JSON.parseObject(body, MessageInfo.class);
                //新增操作
                if (info.getMethod() == MessageInfo.METHOD_ADD) {
                    //转换对象
                    List<EsItem> esItemList = JSON.parseArray(info.getContext().toString(), EsItem.class);
                    //导入索引库
                    itemSearchService.importList(esItemList);
                } else if (info.getMethod() == MessageInfo.METHOD_DELETE) {
                    List<Long> idArray = JSON.parseArray(info.getContext().toString(), Long.class);
                    //list转数组
                    Long[] ids = new Long[idArray.size()];
                    idArray.toArray(ids);
                    //删除索引库
                    itemSearchService.deleteByGoodsId(ids);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回消息读取状态
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
