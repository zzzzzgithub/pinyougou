package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.TbSpecificationMapper;
import com.pinyougou.mapper.TbSpecificationOptionMapper;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.pojogroup.Specification;
import com.pinyougou.service.SpecificationService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * 业务逻辑实现
 *
 * @author Steven
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private TbSpecificationMapper specificationMapper;
    @Autowired
    private TbSpecificationOptionMapper optionMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbSpecification> findAll() {
        return specificationMapper.select(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize, TbSpecification specification) {
        PageResult<TbSpecification> result = new PageResult<TbSpecification>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //构建查询条件
        Example example = new Example(TbSpecification.class);
        Example.Criteria criteria = example.createCriteria();

        if (specification != null) {
            //如果字段不为空
            if (specification.getSpecName() != null && specification.getSpecName().length() > 0) {
                criteria.andLike("specName", "%" + specification.getSpecName() + "%");
            }

        }

        //查询数据
        List<TbSpecification> list = specificationMapper.selectByExample(example);
        //返回数据列表
        result.setRows(list);

        //获取总页数
        PageInfo<TbSpecification> info = new PageInfo<TbSpecification>(list);
        result.setPages(info.getPages());

        return result;
    }

    /**
     * 增加
     */
    @Override
    public void add(Specification specification) {
        //保存规格
        specificationMapper.insertSelective(specification.getSpecification());
        //保存规格选项
        for (TbSpecificationOption option : specification.getSpecificationOptionList()) {
            //设置规格id
            option.setSpecId(specification.getSpecification().getId());
            optionMapper.insertSelective(option);
        }
    }


    /**
     * 修改
     */
    @Override
    public void update(Specification specification) {
        //更新规格名称
        specificationMapper.updateByPrimaryKeySelective(specification.getSpecification());
        //更新规格选项列表
        //先删除所有的选项,再一起添加
        TbSpecificationOption where = new TbSpecificationOption();
        where.setSpecId(specification.getSpecification().getId());
        optionMapper.delete(where);
        for (TbSpecificationOption option : specification.getSpecificationOptionList()) {
            option.setSpecId(specification.getSpecification().getId());
            //保存选项
            optionMapper.insertSelective(option);
        }
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public Specification getById(Long id) {
        Specification result = new Specification();
        //查询规格
        TbSpecification tbSpecification = specificationMapper.selectByPrimaryKey(id);
        result.setSpecification(tbSpecification);
        //查询规格选项
        TbSpecificationOption where = new TbSpecificationOption();
        where.setSpecId(id);
        List<TbSpecificationOption> options = optionMapper.select(where);
        result.setSpecificationOptionList(options);
        return result;
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        //数组转list
        List longs = Arrays.asList(ids);
        //构建查询条件
        Example example = new Example(TbSpecification.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", longs);

        //跟据查询条件删除数据
        specificationMapper.deleteByExample(example);

        //删除关联数据规格选项
       /* for (Long id : ids) {
            TbSpecificationOption where = new TbSpecificationOption();
            where.setSpecId(id);
            optionMapper.delete(where);
        }*/
        Example example1 = new Example(TbSpecificationOption.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andIn("id", longs);
        optionMapper.deleteByExample(example1);
    }


}
