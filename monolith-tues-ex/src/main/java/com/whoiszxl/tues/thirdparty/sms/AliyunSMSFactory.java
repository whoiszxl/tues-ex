package com.whoiszxl.tues.thirdparty.sms;

import com.whoiszxl.tues.common.bean.Result;

/**
 * 阿里云短信工厂
 *
 * @author whoiszxl
 * @date 2021/3/17
 */
public class AliyunSMSFactory extends AbstractSMSFactory {

    @Override
    protected Result specificSend(String mobile, String code) {
        return null;
    }
}
