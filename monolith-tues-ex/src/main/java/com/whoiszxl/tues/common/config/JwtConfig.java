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

    String[] apiPatterns = new String[]{
            "/**/login",
            "/**/sendVerifySms",
            "/**/register",
            "/home/**"
    };

    String[] swaggerPatterns = new String[]{
            "/webjars/**",
            "/swagger-ui/index.html",
            "/swagger-resources/**",
            "/v3/**",
            "/swagger-ui/**",
    };

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(jwtInterceptor) //添加jwt拦截器
                .addPathPatterns("/**") //过滤规则
                .excludePathPatterns(apiPatterns)
                .excludePathPatterns(swaggerPatterns); //排除地址规则
    }
}
