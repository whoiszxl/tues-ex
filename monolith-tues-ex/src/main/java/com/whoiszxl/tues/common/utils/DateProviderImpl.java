package com.whoiszxl.tues.common.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 日期服务实现
 *
 * @author whoiszxl
 * @date 2021/3/24
 */
@Component
public class DateProviderImpl implements DateProvider {

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
