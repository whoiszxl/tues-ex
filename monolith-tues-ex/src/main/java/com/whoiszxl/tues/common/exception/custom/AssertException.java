package com.whoiszxl.tues.common.exception.custom;

/**
 * 断言异常
 *
 * @author zhouxiaolong
 * @date 2021/3/17
 */
public class AssertException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public AssertException() {
        super();
    }
    public AssertException(String message) {
        super(message);
    }
}