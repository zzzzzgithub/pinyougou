package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.entrty.PageResult;
import com.pinyougou.entrty.Result;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.service.BrandService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Id;
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


   /**
    * 分页查询品牌
    * @param pageNo
    * @param pageSize
    * @return
    */
//   @RequestMapping("/findPage")
//   public PageResult<TbBrand> findPage(Integer pageNo, Integer pageSize){
//      return brandService.findPage(pageNo,pageSize);
//   }

   /***
    * 新增品牌
    * @return
    */
   @RequestMapping("/add")
   public Result add(@RequestBody TbBrand tbBrand){
      try {
         brandService.add(tbBrand);
         return new Result(true,"添加品牌成功");
      } catch (Exception e) {
         e.printStackTrace();
      }
      return new Result(false,"添加品牌失败");
   }

   /***
    * 获取要修改品牌的id
    * @param id
    * @return
    */
   @RequestMapping("/getById")
   public TbBrand getById(Long id){
     return brandService.getById(id);
   }


   @RequestMapping("/update")
   public Result update(@RequestBody TbBrand brand){
      try {
         brandService.update(brand);
         return new Result(true,"修改品牌成功");
      } catch (Exception e) {
         e.printStackTrace();
      }
      return new Result(false,"修改品牌失败");
   }

   /***
    * 根据id删除品牌
    * @param ids
    * @return
    */
   @RequestMapping("/deleteById")
   public Result deleteById(Long[] ids){
      try {
         brandService.deleteById(ids);
         return new Result(true,"删除品牌成功");
      } catch (Exception e) {
         e.printStackTrace();
         return new Result(false,"删除品牌失败");
      }
   }

//   @RequestMapping("/findPage")
//   public PageResult<TbBrand> findPage(Integer pageNo,Integer pageSize,@RequestBody TbBrand brand){
//        return brandService.findPage(pageNo,pageSize,brand);
//   }
@RequestMapping("findPage")
public PageResult<TbBrand> findPage(Integer pageNo, Integer pageSize,@RequestBody TbBrand brand) {
   return brandService.findPage(pageNo, pageSize,brand);
}
}
