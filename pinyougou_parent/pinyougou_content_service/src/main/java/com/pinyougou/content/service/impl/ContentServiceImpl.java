package com.pinyougou.content.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.content.service.ContentService;
import com.pinyougou.mapper.TbContentMapper;
import com.pinyougou.pojo.TbContent;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * 业务逻辑实现
 *
 * @author Steven
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;
    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 查询全部
     */
    @Override
    public List<TbContent> findAll() {
        return contentMapper.select(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize, TbContent content) {
        PageResult<TbContent> result = new PageResult<TbContent>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //构建查询条件
        Example example = new Example(TbContent.class);
        Example.Criteria criteria = example.createCriteria();

        if (content != null) {
            //如果字段不为空
            if (content.getTitle() != null && content.getTitle().length() > 0) {
                criteria.andLike("title", "%" + content.getTitle() + "%");
            }
            //如果字段不为空
            if (content.getUrl() != null && content.getUrl().length() > 0) {
                criteria.andLike("url", "%" + content.getUrl() + "%");
            }
            //如果字段不为空
            if (content.getPic() != null && content.getPic().length() > 0) {
                criteria.andLike("pic", "%" + content.getPic() + "%");
            }
            //如果字段不为空
            if (content.getStatus() != null && content.getStatus().length() > 0) {
                criteria.andLike("status", "%" + content.getStatus() + "%");
            }

        }

        //查询数据
        List<TbContent> list = contentMapper.selectByExample(example);
        //返回数据列表
        result.setRows(list);

        //获取总页数
        PageInfo<TbContent> info = new PageInfo<TbContent>(list);
        result.setPages(info.getPages());

        return result;
    }

    /**
     * 增加
     */
    @Override
    public void add(TbContent content) {
        contentMapper.insertSelective(content);
    }


    /**
     * 修改
     */
    @Override
    public void update(TbContent content) {
        contentMapper.updateByPrimaryKeySelective(content);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbContent getById(Long id) {
        return contentMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        //数组转list
        List longs = Arrays.asList(ids);
        //构建查询条件
        Example example = new Example(TbContent.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", longs);

        //跟据查询条件删除数据
        contentMapper.deleteByExample(example);
    }

    @Override
    public List<TbContent> findByCategoryId(Long categoryId) {
        //先从缓存中查询广告列表
        List<TbContent> contents = (List<TbContent>) redisTemplate.boundHashOps("content").get(categoryId);
        if (contents == null || contents.size() < 1) {
            //设置查询条件
            Example example = new Example(TbContent.class);
            Example.Criteria criteria = example.createCriteria();
            //查询正常状态的数据
            criteria.andEqualTo("categoryId", categoryId);
            criteria.andEqualTo("status", "1");
            //设置排序,多个字段逗号隔开
            example.setOrderByClause("sortOrder asc");
            contents = contentMapper.selectByExample(example);
            //把广告放入缓存中
            redisTemplate.boundHashOps("content").put(categoryId, contents);
        } else {
            System.out.println("从缓存中加载了广告数据");
        }
        return contents;
    }

}
