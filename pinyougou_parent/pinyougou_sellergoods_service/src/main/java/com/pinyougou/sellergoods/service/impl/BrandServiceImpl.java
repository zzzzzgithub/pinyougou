package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.entrty.PageResult;
import com.pinyougou.entrty.Result;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import sun.java2d.loops.TransformBlit;

import java.util.Arrays;
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

   /* @Override
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
    }*/

    /***
     * 增加品牌方法
     * @param tbBrand
     */

    @Override
    public void add(TbBrand tbBrand) {
        brandMapper.insertSelective(tbBrand);
    }

    /***
     * 根据id查找要修改的品牌
     * @param id
     * @return
     */
    @Override
    public TbBrand getById(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    /***
     * 保存修改品牌
     * @param brand
     */
    @Override
    public void update(TbBrand brand) {
        brandMapper.updateByPrimaryKeySelective(brand);
    }

    /***
     * 根据id删除品牌
     * @param ids
     */
    @Override
    public void deleteById(Long[] ids) {
        //数组转list
        List longs = Arrays.asList(ids);
        //构造查询条件
        Example example=new Example(TbBrand.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id",longs);
        //根据条件删除数据
        brandMapper.deleteByExample(example);
    }

//    @Override
//    public PageResult<TbBrand> findPage(Integer pageNum, Integer pageSize, TbBrand brand) {
//        PageResult<TbBrand> page=new PageResult<>();
//        //设置分页条件
//        PageHelper.startPage(pageNum,pageSize);
//        //构造查询条件
//        Example example=new Example(TbBrand.class);
//        Example.Criteria criteria = example.createCriteria();
//        //如果名称不为空
//        if (brand.getName()!=null && brand.getName().trim().length()>0) {
//            criteria.andLike("name","%"+brand.getName()+"%");
//        }
//        //如果首字母不为空
//        if (brand.getFirstChar() !=null && brand.getFirstChar().trim().length()>0) {
//            criteria.andEqualTo("firstChar",brand.getFirstChar());
//        }
//        //查询数据
//        List<TbBrand> brandList = brandMapper.selectByExample(example);
//        //保存数据列表
//        page.setRows(brandList);
//        //获取总页数
//        PageInfo<TbBrand> info=new PageInfo<>(brandList);
//        page.setPages(info.getPages());
//        return page;
//    }
@Override
public PageResult<TbBrand> findPage(Integer pageNum, Integer pageSize,TbBrand brand) {
    PageResult<TbBrand> page = new PageResult<>();
    //设置分页条件
    PageHelper.startPage(pageNum, pageSize);
    /*List<TbBrand> brandList = brandMapper.select(brand);*/
    Example example = new Example(TbBrand.class);
    Example.Criteria criteria = example.createCriteria();
    //组装查询条件
    if(brand != null){
        //品牌名称过滤
        if(brand.getName() != null && brand.getName().trim().length() > 0){
            criteria.andLike("name", "%" + brand.getName() + "%");
        }

        //首字母过滤
        if(brand.getFirstChar() != null && brand.getFirstChar().trim().length() > 0){
            criteria.andEqualTo("firstChar", brand.getFirstChar());
        }
    }
    List<TbBrand> brandList = brandMapper.selectByExample(example);

    page.setRows(brandList);

    //返回总页数
    PageInfo<TbBrand> info = new PageInfo<>(brandList);
    page.setPages(info.getPages());

    return page;
}


}
