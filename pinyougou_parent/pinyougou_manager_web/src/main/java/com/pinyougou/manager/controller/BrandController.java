package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.service.BrandService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 品牌请求处理器
 * @author Steven
 */
@RestController
@RequestMapping("/brand")
public class BrandController {
    @Reference
   private BrandService brandService;
   /**
    * 返回全部列表
    * @return
    */
   @RequestMapping("/findAll")
   public List<TbBrand> findAll(){
      return brandService.findAll();
   }
}
