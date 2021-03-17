package com.whoiszxl.tues.common.config;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * jwt配置
 *
 * @author zhouxiaolong
 */
public class JwtConfig extends WebMvcConfigurationSupport {


    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
    }
}
