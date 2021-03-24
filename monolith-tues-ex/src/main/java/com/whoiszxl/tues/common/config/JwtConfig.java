package com.whoiszxl.tues.common.config;

import com.whoiszxl.tues.common.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * jwt配置
 *
 * @author whoiszxl
 */
@Configuration
public class JwtConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    String[] apiPatterns = new String[]{
            "/**/login",
            "/**/sendVerifySms",
            "/**/register",
            "/home/**",
    };

    String[] swaggerPatterns = new String[]{
            "/swagger-resources/**",
            "/webjars/**",
            "/v3/**",
            "/swagger-ui.html/**",
            "/swagger-ui/**"
    };

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        InterceptorRegistration registration = registry.addInterceptor(jwtInterceptor);
        registration.addPathPatterns("/**");
        registration.excludePathPatterns();
        registration.excludePathPatterns(apiPatterns);
    }
}
