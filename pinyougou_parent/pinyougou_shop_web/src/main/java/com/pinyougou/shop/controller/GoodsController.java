package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojogroup.Goods;
import com.pinyougou.cart.service.GoodsService;
import entity.PageResult;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 请求处理器
 *
 * @author Steven
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference
    private GoodsService goodsService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbGoods> findAll() {
        return goodsService.findAll();
    }


    /**
     * 分页查询数据
     *
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(int pageNo, int pageSize, @RequestBody TbGoods goods) {
        //获取商家id
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        //添加查询条件
        goods.setSellerId(sellerId);
        return goodsService.findPage(pageNo, pageSize, goods);
    }

    /**
     * 增加
     *
     * @param goods
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Goods goods) {
        try {
            //获取当前登录用户id
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            goods.getGoods().setSellerId(username);
            goodsService.add(goods);
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param goods
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody Goods goods) {
        //验证修改权限,商家只能修改自己的商品
        Goods beUpdate = goodsService.getById(goods.getGoods().getId());
        //获取当前登录用户的id
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        //判断是否是商品卖家修改
        if (sellerId.equals(beUpdate.getGoods().getSellerId()) || !sellerId.equals(goods.getGoods().getSellerId())) {
            return new Result(false, "非法操作");

        }
        try {
            goodsService.update(goods);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/getById")
    public Goods getById(Long id) {
        return goodsService.getById(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            goodsService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

}
