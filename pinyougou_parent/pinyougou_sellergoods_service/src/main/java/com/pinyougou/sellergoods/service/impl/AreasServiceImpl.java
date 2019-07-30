package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.TbAreasMapper;
import com.pinyougou.pojo.TbAreas;
import com.pinyougou.cart.service.AreasService;
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
public class AreasServiceImpl implements AreasService {

	@Autowired
	private TbAreasMapper areasMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbAreas> findAll() {
		return areasMapper.select(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize,TbAreas areas) {
		PageResult<TbAreas> result = new PageResult<TbAreas>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //构建查询条件
        Example example = new Example(TbAreas.class);
        Example.Criteria criteria = example.createCriteria();
		
		if(areas!=null){			
						//如果字段不为空
			if (areas.getAreaid()!=null && areas.getAreaid().length()>0) {
				criteria.andLike("areaid", "%" + areas.getAreaid() + "%");
			}
			//如果字段不为空
			if (areas.getArea()!=null && areas.getArea().length()>0) {
				criteria.andLike("area", "%" + areas.getArea() + "%");
			}
			//如果字段不为空
			if (areas.getCityid()!=null && areas.getCityid().length()>0) {
				criteria.andLike("cityid", "%" + areas.getCityid() + "%");
			}
	
		}

        //查询数据
        List<TbAreas> list = areasMapper.selectByExample(example);
        //返回数据列表
        result.setRows(list);

        //获取总页数
        PageInfo<TbAreas> info = new PageInfo<TbAreas>(list);
        result.setPages(info.getPages());
		
		return result;
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbAreas areas) {
		areasMapper.insertSelective(areas);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbAreas areas){
		areasMapper.updateByPrimaryKeySelective(areas);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbAreas getById(Long id){
		return areasMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		//数组转list
        List longs = Arrays.asList(ids);
        //构建查询条件
        Example example = new Example(TbAreas.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", longs);

        //跟据查询条件删除数据
        areasMapper.deleteByExample(example);
	}
	
	
}
