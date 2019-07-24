package com.pinyougou.search.service;

import java.util.List;
import java.util.Map;

public interface ItemSearchService {

    /**
     * 搜索方法
     *
     * @param searchMap 查询条件列表
     * @return 结果集，除了商品列表，还包含规格等等信息
     */
    public Map search(Map searchMap);

    /**
     * 批量导入数据
     *
     * @param list
     */
    public void importList(List list);

    /**
     * 跟据id列表删除索引
     *
     * @param goodsIdList
     */
    public void deleteByGoodsId(Long[] goodsIdList);


}
