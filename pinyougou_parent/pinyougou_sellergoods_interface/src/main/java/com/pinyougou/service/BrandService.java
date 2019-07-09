package com.pinyougou.service;

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
}
