package com.pinyougou.pay.service;

import java.util.Map;

/**
 * 微信支付接口
 * @author pdd
 * @version 1.0
 * @description com.pinyougou.pay.service
 * @date 2019-08-02
 */
public interface WeixinPayService {
    /**
     * 生成微信支付二维码
     * @param out_trade_no 订单号
     * @param total_fee 金额(分)
     * @return
     */
    public Map createNative(String out_trade_no, String total_fee);

    /**
     * 查询支付状态
     * @param out_trade_no 商户订单号
     */
    public Map queryPayStatus(String out_trade_no);

}
