package com.pinyougou.service;
import java.util.List;
import com.pinyougou.pojo.TbContent;

import entity.PageResult;
/**
 * 业务逻辑接口
 * @author Steven
 *
 */
public interface ContentService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbContent> findAll();
	
	
	/**
     * 分页查询列表
     * @return
     */
    public PageResult<TbContent> findPage(int pageNum, int pageSize, TbContent content);
	
	
	/**
	 * 增加
	*/
	public void add(TbContent content);
	
	
	/**
	 * 修改
	 */
	public void update(TbContent content);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbContent getById(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long[] ids);

	
}
