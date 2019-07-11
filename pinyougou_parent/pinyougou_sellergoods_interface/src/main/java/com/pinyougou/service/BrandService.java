package com.pinyougou.service;


import com.pinyougou.entrty.PageResult;
import com.pinyougou.entrty.Result;
import com.pinyougou.pojo.TbBrand;

import java.util.List;

/**
 * 品牌业务逻辑接口
 * @author Steven
 *
 */
public interface BrandService {
   /**
    * 查询所有品牌
    * @return
    */
   public List<TbBrand> findAll();

   /**
    * 分页查询品牌列表
    * @return
    */
  // public PageResult<TbBrand> findPage(int pageNum, int pageSize);

   /***
    * 增加品牌方法
    * @param tbBrand
    */
    public void add(TbBrand tbBrand);

   /**
    * 跟据id查询数据
    * @param id
    * @return
    */
   public TbBrand getById(Long id);

   /**
    * 修改品牌
    * @param brand
    */
   public void update(TbBrand brand);

   /***
    * 删除品牌
    * @param ids
    */
   public void deleteById(Long[] ids);

   /***
    * 查询品牌
    * @param pageNum
    * @param pageSize
    * @param brand
    * @return
    */
   //public PageResult<TbBrand> findPage(Integer pageNum,Integer pageSize,TbBrand brand);


   /**
    * 分页查询
    * @param pageNum
    * @param pageSize
    * @return
    */
   public PageResult<TbBrand> findPage(Integer pageNum,Integer pageSize,TbBrand brand);
}
