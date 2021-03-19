package com.whoiszxl.tues.common.config;

import com.whoiszxl.tues.common.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * jwt配置
 *
 * @author zhouxiaolong
 */
@Configuration
public class JwtConfig extends WebMvcConfigurationSupport {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(jwtInterceptor) //添加jwt拦截器
                .addPathPatterns("/**") //过滤规则
                .excludePathPatterns("/**/login","/**/sendVerifySms","/**/register"); //排除地址规则
    }
}
