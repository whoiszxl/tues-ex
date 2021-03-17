package com.whoiszxl.tues.common.exception.custom;

import com.whoiszxl.tues.common.bean.Result;

/**
 * 校验异常
 *
 * @author zhouxiaolong
 * @date 2021/3/17
 */
public class ValidateException extends RuntimeException {

    //错误代码
    Result result;

    public ValidateException(Result result){
        this.result = result;
    }
    public Result getResult(){
        return result;
    }
}