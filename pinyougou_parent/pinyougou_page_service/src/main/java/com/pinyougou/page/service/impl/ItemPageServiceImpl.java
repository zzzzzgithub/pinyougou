package com.pinyougou.page.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.abel533.entity.Example;
import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.page.service.ItemPageService;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pdd
 * @version 1.0
 * @description com.pinyougou.page.service.impl
 * @date 2019-7-24
 */
@Service
public class ItemPageServiceImpl implements ItemPageService {
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Autowired
    private TbGoodsMapper goodsMapper;
    @Autowired
    private TbGoodsDescMapper goodsDescMapper;
    @Autowired
    private TbItemMapper itemMapper;
    @Autowired

    private TbItemCatMapper itemCatMapper;
    @Value("${PAGEDIR}")
    private String PAGEDIR;

    @Override
    public boolean genItemHtml(Long goodsId) {
        try {
            //读取模板对象
            Configuration cfg = freeMarkerConfigurer.getConfiguration();
            Template template = cfg.getTemplate("item.ftl");
            //构建数据模型对象
            Map map = new HashMap();
            //查询商品基本信息
            TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
            map.put("goods", goods);
            //查询商品描述信息
            TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
            map.put("goodsDesc", goodsDesc);
            //查询商品分类
            String category1Name = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
            String category2Name = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
            String category3Name = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();
            map.put("category1Name", category1Name);
            map.put("category2Name", category2Name);
            map.put("category3Name", category3Name);
            //查询商品sku列表
            Example example = new Example(TbItem.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("goodsId", goodsId);//指定skuID
            criteria.andEqualTo("status", "1");//只查询有效的sku
            example.setOrderByClause("isDefault desc");//按默认降序,为第一个选中默认的
            List<TbItem> itemList = itemMapper.selectByExample(example);
            map.put("itemList", itemList);
            //输出静态html
            Writer out = new FileWriter(PAGEDIR + goodsId + ".html");
            template.process(map, out);
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
