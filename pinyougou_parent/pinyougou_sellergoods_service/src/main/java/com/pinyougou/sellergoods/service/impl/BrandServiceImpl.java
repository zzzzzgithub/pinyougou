package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.entity.PageResult;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author steven
 * @version 1.0
 * @description com.pinyougou.sellergoods.service.impl
 */
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private TbBrandMapper brandMapper;
    @Override
    public List<TbBrand> findAll() {
        return brandMapper.select(null);
    }

    @Override
    public PageResult<TbBrand> findPage(int pageNum, int pageSize) {
        PageResult<TbBrand> result=new PageResult<TbBrand>();
        //设置分页条件
        PageHelper.startPage(pageNum,pageSize);
        //查询数据
        List<TbBrand> brands = brandMapper.select(null);
        //保存数据列表
        result.setRows(brands);
        //获取总页数
        PageInfo<TbBrand> info = new PageInfo<>(brands);
        result.setPages(info.getPages());
        return result;
    }
}
