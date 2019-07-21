package com.pinyougou.es.dao;

import entity.EsItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 商品信息ES接口
 * @author pdd
 * @version 1.0
 * @description com.itheima.es.dao
 * @date 2019-07-19
 */
public interface ItemDao extends ElasticsearchRepository<EsItem,Long> {
}
