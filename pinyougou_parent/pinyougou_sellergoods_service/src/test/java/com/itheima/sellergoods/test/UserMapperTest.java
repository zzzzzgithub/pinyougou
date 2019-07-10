package com.itheima.sellergoods.test;

import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.UserMapper;
import com.pinyougou.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext-*.xml")
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testGetAllUser() {
        //查询所有用户
        List<User> select = userMapper.select(null);
        for (User user : select) {
            System.out.println(user);
        }
    }

    @Test
    //不忽略空值插入
    public void testInsert() {
        User user = new User();
        user.setUsername("肖武");
        user.setAddress("邵阳");
        user.setBirthday(new Date());
        user.setSex("女");
        userMapper.insert(user);
    }

    @Test
    //插入数据,忽略空值
    public void testInsertSelective() {
        User user = new User();
        user.setUsername("风一般的男子");
        user.setSex("女");
        user.setAddress("湖北");
        userMapper.insertSelective(user);
    }


    @Test
    //根据主键修改数据-不忽略空值
    public void testUpdateByPrimaryKey() {
        User user = new User();
        user.setId(37);
        user.setUsername("刘文建");
        user.setBirthday(new Date());
        user.setSex("1");
        user.setAddress("湖北");
        userMapper.updateByPrimaryKey(user);
    }

    @Test
    //根据主键修噶数据-忽略空值
    public void testUpdateByPrimaryKeySelective(){
        User user = new User();
        user.setId(38);
        user.setUsername("娃哈哈");
        user.setBirthday(new Date());
        userMapper.updateByPrimaryKeySelective(user);
    }


    @Test
    //根据条件修改数据
    public void testUpdateByExample(){
        User user = new User();
        user.setUsername("鲁亮");
        user.setBirthday(new Date());
        user.setSex("1");
        user.setAddress("龙山湖");
        //构造查询条件
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        List ids = Arrays.asList(37, 38);
        criteria.andIn("id",ids);
        //不忽略空值
        //userMapper.updateByExample(user,example);
        //忽略空值
        userMapper.updateByExampleSelective(user,example);
    }

    @Test
    //根据主键查询
    public void testSelectByPrimaryKey(){
        User user = userMapper.selectByPrimaryKey(39);
        System.out.println(user);
    }

    @Test
    //查询一条数据
    public void testSelectOne(){
        User one = new User();
        one.setUsername("赵子龙");
        //查询数据
        User user = userMapper.selectOne(one);
        System.out.println(user);
    }

    @Test
    //根据查询条件-JavaBean
    public void testSelect(){
        User user = new User();
        //user.setUsername("鲁亮");
        //user.setAddress("深圳黑马");
        user.setSex("0");
        //查询数据
        List<User> users = userMapper.select(user);
        for (User user1 : users) {
            System.out.println(user1);
        }
    }

    @Test
    //根据查询条件-Example
    public void testSelectByExample(){
        //构件查询条件
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        //建立like查询
        //criteria.andLike("address","%黑马%");
        criteria.andLike("username","%小%");
        //查询数据
        List<User> userList = userMapper.selectByExample(example);
        for (User user : userList) {
            System.out.println(user);
        }
    }

    @Test
    //查询记录数
    public void testSelectCount(){
        int count = userMapper.selectCount(null);
        System.out.println("总记录数:   "+count);
    }

    @Test
    //根据主键删除
    public void testDeleteByPrimarykey(){
        userMapper.deleteByPrimaryKey(38);
    }

    @Test
    //条件删除-JavaBean
    public void testDelete(){
        //删除条件
        User by = new User();
        by.setUsername("貂蝉");
        int delete = userMapper.delete(by);
        System.out.println("删除了数据: "+delete);
    }

    @Test
    //条件删除-Example
    public void testDeleteByExample(){
        //构造删除条件
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("address","address");
        //删除数据
        userMapper.deleteByExample(example);
    }

    @Test
    //分页查询
    public void testSelectByPage(){
        //设置分页条件
        PageHelper.startPage(1,5);
        //查询分页列表
        List<User> userList = userMapper.select(null);
        for (User user : userList) {
            System.out.println(user);
        }
        //查询分页信息
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        System.out.println("总记录数为:  "+pageInfo.getTotal());
    }
}
