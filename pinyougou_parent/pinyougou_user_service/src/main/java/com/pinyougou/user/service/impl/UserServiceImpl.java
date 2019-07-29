package com.pinyougou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.TbUserMapper;
import com.pinyougou.pojo.TbUser;
import com.pinyougou.user.service.UserService;
import entity.PageResult;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 业务逻辑实现
 *
 * @author Steven
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DefaultMQProducer producer;


    /**
     * 查询全部
     */
    @Override
    public List<TbUser> findAll() {
        return userMapper.select(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize, TbUser user) {
        PageResult<TbUser> result = new PageResult<TbUser>();
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);

        //构建查询条件
        Example example = new Example(TbUser.class);
        Example.Criteria criteria = example.createCriteria();

        if (user != null) {
            //如果字段不为空
            if (user.getUsername() != null && user.getUsername().length() > 0) {
                criteria.andLike("username", "%" + user.getUsername() + "%");
            }
            //如果字段不为空
            if (user.getPassword() != null && user.getPassword().length() > 0) {
                criteria.andLike("password", "%" + user.getPassword() + "%");
            }
            //如果字段不为空
            if (user.getPhone() != null && user.getPhone().length() > 0) {
                criteria.andLike("phone", "%" + user.getPhone() + "%");
            }
            //如果字段不为空
            if (user.getEmail() != null && user.getEmail().length() > 0) {
                criteria.andLike("email", "%" + user.getEmail() + "%");
            }
            //如果字段不为空
            if (user.getSourceType() != null && user.getSourceType().length() > 0) {
                criteria.andLike("sourceType", "%" + user.getSourceType() + "%");
            }
            //如果字段不为空
            if (user.getNickName() != null && user.getNickName().length() > 0) {
                criteria.andLike("nickName", "%" + user.getNickName() + "%");
            }
            //如果字段不为空
            if (user.getName() != null && user.getName().length() > 0) {
                criteria.andLike("name", "%" + user.getName() + "%");
            }
            //如果字段不为空
            if (user.getStatus() != null && user.getStatus().length() > 0) {
                criteria.andLike("status", "%" + user.getStatus() + "%");
            }
            //如果字段不为空
            if (user.getHeadPic() != null && user.getHeadPic().length() > 0) {
                criteria.andLike("headPic", "%" + user.getHeadPic() + "%");
            }
            //如果字段不为空
            if (user.getQq() != null && user.getQq().length() > 0) {
                criteria.andLike("qq", "%" + user.getQq() + "%");
            }
            //如果字段不为空
            if (user.getIsMobileCheck() != null && user.getIsMobileCheck().length() > 0) {
                criteria.andLike("isMobileCheck", "%" + user.getIsMobileCheck() + "%");
            }
            //如果字段不为空
            if (user.getIsEmailCheck() != null && user.getIsEmailCheck().length() > 0) {
                criteria.andLike("isEmailCheck", "%" + user.getIsEmailCheck() + "%");
            }
            //如果字段不为空
            if (user.getSex() != null && user.getSex().length() > 0) {
                criteria.andLike("sex", "%" + user.getSex() + "%");
            }

        }

        //查询数据
        List<TbUser> list = userMapper.selectByExample(example);
        //返回数据列表
        result.setRows(list);

        //获取总页数
        PageInfo<TbUser> info = new PageInfo<TbUser>(list);
        result.setPages(info.getPages());

        return result;
    }

    /**
     * 增加
     */
    @Override
    public void add(TbUser user) {
        user.setCreated(new Date());//创建日期
        user.setUpdated(user.getCreated());//修改日期
        //加密密码
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        userMapper.insertSelective(user);
    }


    /**
     * 修改
     */
    @Override
    public void update(TbUser user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbUser getById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        //数组转list
        List longs = Arrays.asList(ids);
        //构建查询条件
        Example example = new Example(TbUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", longs);

        //跟据查询条件删除数据
        userMapper.deleteByExample(example);
    }

    /**
     * 生成验证码并保存到redis
     *
     * @param phone
     */
    @Override
    public void createSmsCode(String phone) {
        //生成6为随机数
        String code = (long) (Math.random() * 100000) + "";
        System.out.println("验证码为:" + code);
        //保存到redis中
        BoundValueOperations ValueOps = redisTemplate.boundValueOps("user_mobile_code" + phone);
        ValueOps.set(code);
        //设置有效期
        ValueOps.expire(1, TimeUnit.HOURS);
        //发送rocketMQ消息
        try {//构建消息内容
            Map<Object, Object> map = new HashMap<>();
            map.put("phone", phone);
            map.put("template_code", "SMS_168592714");
            map.put("sign_name", "郑记牛杂面");
            map.put("param", "{\"code\":" + code + "}");
            //把map转换成string
            String json = JSON.toJSONString(map);
            //创建消息对象
            Message message = new Message(
                    "topic-sms",
                    "tags-sms-test",
                    "keys-sms-test",
                    json.getBytes(RemotingHelper.DEFAULT_CHARSET)
            );
            //发送MQ
            producer.send(message);

        } catch (Exception e) {


        }
    }

    /**
     * 判断验证码是否存在
     *
     * @param phone 手机
     * @param code  验证码
     * @return
     */
    @Override
    public boolean checkSmsCode(String phone, String code) {
        //读取验证码
        String smsCode = (String) redisTemplate.boundValueOps("user_mobile_code_" + phone).get();
        //验证验证码
        return code.equals(smsCode);
    }


}
