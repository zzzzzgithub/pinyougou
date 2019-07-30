package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.cart.service.GoodsDescService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * 业务逻辑实现
 * @author Steven
 *
 */
@Service
public class GoodsDescServiceImpl implements GoodsDescService {

	@Autowired
	private TbGoodsDescMapper goodsDescMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoodsDesc> findAll() {
		return goodsDescMapper.select(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize,TbGoodsDesc goodsDesc) {
		PageResult<TbGoodsDesc> result = new PageResult<TbGoodsDesc>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //构建查询条件
        Example example = new Example(TbGoodsDesc.class);
        Example.Criteria criteria = example.createCriteria();
		
		if(goodsDesc!=null){			
						//如果字段不为空
			if (goodsDesc.getIntroduction()!=null && goodsDesc.getIntroduction().length()>0) {
				criteria.andLike("introduction", "%" + goodsDesc.getIntroduction() + "%");
			}
			//如果字段不为空
			if (goodsDesc.getSpecificationItems()!=null && goodsDesc.getSpecificationItems().length()>0) {
				criteria.andLike("specificationItems", "%" + goodsDesc.getSpecificationItems() + "%");
			}
			//如果字段不为空
			if (goodsDesc.getCustomAttributeItems()!=null && goodsDesc.getCustomAttributeItems().length()>0) {
				criteria.andLike("customAttributeItems", "%" + goodsDesc.getCustomAttributeItems() + "%");
			}
			//如果字段不为空
			if (goodsDesc.getItemImages()!=null && goodsDesc.getItemImages().length()>0) {
				criteria.andLike("itemImages", "%" + goodsDesc.getItemImages() + "%");
			}
			//如果字段不为空
			if (goodsDesc.getPackageList()!=null && goodsDesc.getPackageList().length()>0) {
				criteria.andLike("packageList", "%" + goodsDesc.getPackageList() + "%");
			}
			//如果字段不为空
			if (goodsDesc.getSaleService()!=null && goodsDesc.getSaleService().length()>0) {
				criteria.andLike("saleService", "%" + goodsDesc.getSaleService() + "%");
			}
	
		}

        //查询数据
        List<TbGoodsDesc> list = goodsDescMapper.selectByExample(example);
        //返回数据列表
        result.setRows(list);

        //获取总页数
        PageInfo<TbGoodsDesc> info = new PageInfo<TbGoodsDesc>(list);
        result.setPages(info.getPages());
		
		return result;
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbGoodsDesc goodsDesc) {
		goodsDescMapper.insertSelective(goodsDesc);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbGoodsDesc goodsDesc){
		goodsDescMapper.updateByPrimaryKeySelective(goodsDesc);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbGoodsDesc getById(Long id){
		return goodsDescMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		//数组转list
        List longs = Arrays.asList(ids);
        //构建查询条件
        Example example = new Example(TbGoodsDesc.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", longs);

        //跟据查询条件删除数据
        goodsDescMapper.deleteByExample(example);
	}
	
	
}
