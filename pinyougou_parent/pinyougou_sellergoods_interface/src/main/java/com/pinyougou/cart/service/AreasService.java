package com.pinyougou.cart.service;
import java.util.List;
import com.pinyougou.pojo.TbAreas;

import entity.PageResult;
/**
 * 业务逻辑接口
 * @author Steven
 *
 */
public interface AreasService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbAreas> findAll();
	
	
	/**
     * 分页查询列表
     * @return
     */
    public PageResult<TbAreas> findPage(int pageNum, int pageSize, TbAreas areas);
	
	
	/**
	 * 增加
	*/
	public void add(TbAreas areas);
	
	
	/**
	 * 修改
	 */
	public void update(TbAreas areas);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbAreas getById(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long[] ids);

	
}
