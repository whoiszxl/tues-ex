package com.whoiszxl.tues.common.exception;

import com.whoiszxl.tues.common.bean.Result;
import com.whoiszxl.tues.common.exception.custom.JwtAuthException;
import com.whoiszxl.tues.common.exception.custom.ValidateException;

/**
 * 异常捕获
 *
 * @author zhouxiaolong
 * @date 2021/3/17
 */
public class ExceptionCatcher {

    public static void catchJwtAuthEx(){
        throw new JwtAuthException();
    }

    public static void catchValidateEx(Result result){
        throw new ValidateException(result);
    }

}