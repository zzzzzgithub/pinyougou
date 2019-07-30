package com.pinyougou.cart.service;

import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojogroup.Goods;
import entity.PageResult;

import java.util.List;

/**
 * 业务逻辑接口
 *
 * @author Steven
 */
public interface GoodsService {

    /**
     * 返回全部列表
     *
     * @return
     */
    public List<TbGoods> findAll();


    /**
     * 分页查询列表
     *
     * @return
     */
    public PageResult<TbGoods> findPage(int pageNum, int pageSize, TbGoods goods);


    /**
     * 增加
     */
    public void add(Goods goods);


    /**
     * 修改
     */
    public void update(Goods goods);


    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    public Goods getById(Long id);


    /**
     * 批量删除
     *
     * @param ids
     */
    public void delete(Long[] ids);

    /**
     * 跟据id列表，更新状态
     *
     * @param ids
     * @param status
     */
    public void updateStatus(Long[] ids, String status);

    /**
     * 跟据SPU-ID列表和状态，查询SKU列表
     *
     * @param goodsIds
     * @param status
     * @return
     */
    public List<TbItem> findItemListByGoodsIdsAndStatus(Long[] goodsIds, String status);
}
