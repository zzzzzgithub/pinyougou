package com.pinyougou.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.pinyougou.pay.service.WeixinPayService;
import com.pinyougou.utils.HttpClient;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pdd
 * @version 1.0
 * @date 2019/8/2 20:37
 */
@Service
public class WeixinPayServiceImpl implements WeixinPayService {

    @Value("${appid}")
    private String appid;
    @Value("${partner}")
    private String partner;
    @Value("${partnerkey}")
    private String partnerkey;
    @Value("${notifyurl}")
    private String notifyurl;

    @Override
    public Map createNative(String out_trade_no, String total_fee) {
        try {
            //1.组装请求参数
            Map paramMap = new HashMap();
            paramMap.put("appid", appid);//公众号id
            paramMap.put("mch_id", partner);//商户号
            //生成随机字符串
            String nonce_str = WXPayUtil.generateNonceStr();
            paramMap.put("nonce_str", nonce_str);//随机字符串
            paramMap.put("body", "品优购");//商品描述-用户扫码后看到的信息
            paramMap.put("out_trade_no", out_trade_no);//商户订单号
            paramMap.put("total_fee", total_fee);//支付金额 单位是分 类型是int
            paramMap.put("spbill_create_ip", "127.0.0.1");//终端ip
            paramMap.put("notify_url", notifyurl);//通知地址
            paramMap.put("trade_type", "NATIVE");//交易类型
            String paramXml = WXPayUtil.generateSignedXml(paramMap, partnerkey);
            System.out.println("正在发起统一下单接口成功,请求参数为:" + paramXml);
            //2.通过HttpClient发起微信统一下单请求
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            httpClient.setHttps(true);//使用加密协议
            //设置方法入参
            httpClient.setXmlParam(paramXml);
            httpClient.post();
            String content = httpClient.getContent();//读取内容
            System.out.println("发起统一下单接口成功，响应参数为：" + content);
            //解释内容吧xml转成map对象
            Map<String, String> resultMap = WXPayUtil.xmlToMap(content);
            //3.读取与接受结果哦,组装返回
            Map map = new HashMap();
            map.put("code_url", resultMap.get("code_url"));//二维码内容
            map.put("out_trade_no", out_trade_no);//商户支付订单号
            map.put("total_fee", total_fee);//支付总金额
            return map;
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public Map queryPayStatus(String out_trade_no) {
        try {
            //1、包装微信接口需要的参数
            Map param = new HashMap();
            param.put("appid", appid);  //公众号ID
            param.put("mch_id", partner);  //商户号
            param.put("nonce_str", WXPayUtil.generateNonceStr()); //随机字符串
            param.put("out_trade_no", out_trade_no); //订单号
            //2、生成xml，通过httpClient发送请求得到数据
            String xmlParam = WXPayUtil.generateSignedXml(param, partnerkey);
            System.out.println("请求参数:" + xmlParam);
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            httpClient.setHttps(true);
            httpClient.setXmlParam(xmlParam);
            httpClient.post();
            //3、解析结果
            String xmlResult = httpClient.getContent();
            System.out.println("微信返回结果：" + xmlResult);
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xmlResult);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
