package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.TbSpecificationOptionMapper;
import com.pinyougou.mapper.TbTypeTemplateMapper;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.pojo.TbTypeTemplate;
import com.pinyougou.service.TypeTemplateService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 业务逻辑实现
 *
 * @author pdd
 */
@Service
public class TypeTemplateServiceImpl implements TypeTemplateService {

    @Autowired
    private TbTypeTemplateMapper typeTemplateMapper;

    @Autowired
    private TbSpecificationOptionMapper optionMapper;
    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 查询全部
     */
    @Override
    public List<TbTypeTemplate> findAll() {
        return typeTemplateMapper.select(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize, TbTypeTemplate typeTemplate) {
        PageResult<TbTypeTemplate> result = new PageResult<TbTypeTemplate>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //构建查询条件
        Example example = new Example(TbTypeTemplate.class);
        Example.Criteria criteria = example.createCriteria();

        if (typeTemplate != null) {
            //如果字段不为空
            if (typeTemplate.getName() != null && typeTemplate.getName().length() > 0) {
                criteria.andLike("name", "%" + typeTemplate.getName() + "%");
            }
            //如果字段不为空
            if (typeTemplate.getSpecIds() != null && typeTemplate.getSpecIds().length() > 0) {
                criteria.andLike("specIds", "%" + typeTemplate.getSpecIds() + "%");
            }
            //如果字段不为空
            if (typeTemplate.getBrandIds() != null && typeTemplate.getBrandIds().length() > 0) {
                criteria.andLike("brandIds", "%" + typeTemplate.getBrandIds() + "%");
            }
            //如果字段不为空
            if (typeTemplate.getCustomAttributeItems() != null && typeTemplate.getCustomAttributeItems().length() > 0) {
                criteria.andLike("customAttributeItems", "%" + typeTemplate.getCustomAttributeItems() + "%");
            }

        }

        //查询数据
        List<TbTypeTemplate> list = typeTemplateMapper.selectByExample(example);
        //返回数据列表
        result.setRows(list);

        //获取总页数
        PageInfo<TbTypeTemplate> info = new PageInfo<TbTypeTemplate>(list);
        result.setPages(info.getPages());
        saveToRedis();//把数据放入缓存
        return result;
    }

    /**
     * 增加
     */
    @Override
    public void add(TbTypeTemplate typeTemplate) {
        typeTemplateMapper.insertSelective(typeTemplate);
    }


    /**
     * 修改
     */
    @Override
    public void update(TbTypeTemplate typeTemplate) {
        typeTemplateMapper.updateByPrimaryKeySelective(typeTemplate);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbTypeTemplate getById(Long id) {
        return typeTemplateMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        //数组转list
        List longs = Arrays.asList(ids);
        //构建查询条件
        Example example = new Example(TbTypeTemplate.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", longs);

        //跟据查询条件删除数据
        typeTemplateMapper.deleteByExample(example);
    }

    /**
     * 跟据模板id查询规格列表
     *
     * @param id
     * @return
     */
    @Override
    public List<Map> findSpecList(Long id) {
        //查询模板
        TbTypeTemplate typeTemplate = typeTemplateMapper.selectByPrimaryKey(id);
        //把json串转成list<Map>
        List<Map> list = JSON.parseArray(typeTemplate.getSpecIds(), Map.class);

        TbSpecificationOption where;
        //遍历规格列表,查询规格选项列表
        for (Map map : list) {
            //组装查询条件,根据规格id查询谷歌选项列表
            where = new TbSpecificationOption();
            where.setSpecId(new Long(map.get("id").toString()));
            List<TbSpecificationOption> options = optionMapper.select(where);
            //保存数据到结果列表中
            map.put("options", options);
        }
        return list;
    }

    /**
     * 将数据放入缓存
     */
    private void saveToRedis() {
        //将商品分类数据放入redis,以分类名称作为key,以模板id作为值
        List<TbTypeTemplate> templates =this.findAll();
        for (TbTypeTemplate template : templates) {
            //缓存品牌列表
            List<Map> brandList = JSON.parseArray(template.getBrandIds(), Map.class);
            redisTemplate.boundHashOps("brandList").put(template.getId(), brandList);
            //缓存规格列表
            List<Map> specList = findSpecList(template.getId());
            redisTemplate.boundHashOps("specList").put(template.getId(), specList);
        }
    }
}
