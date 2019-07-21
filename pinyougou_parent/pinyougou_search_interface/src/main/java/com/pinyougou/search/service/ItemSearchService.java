package com.pinyougou.search.service;

import java.util.Map;

public interface ItemSearchService {

    /**
     * 搜索方法
     * @param searchMap 查询条件列表
     * @return 结果集，除了商品列表，还包含规格等等信息
     */
    public Map search(Map searchMap);

}
