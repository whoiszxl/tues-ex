package com.whoiszxl.tues.common.interceptor;

import cn.hutool.core.util.StrUtil;
import com.whoiszxl.tues.common.enums.MemberRoleEnum;
import com.whoiszxl.tues.common.exception.custom.JwtAuthException;
import com.whoiszxl.tues.common.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT校验拦截器
 *
 * @author whoiszxl
 */
@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {


    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 对header中的token进行解析，始终放行，拦截器仅做解析验证，逻辑判断在controller下再操作。
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("jwt 拦截器运行了");
        final String header = request.getHeader("Authorization");
        if(StrUtil.isNotBlank(header) && header.startsWith("Bearer ")) {
            final String token = header.substring(7);

            //对令牌进行验证
            try {
                Claims claims = jwtUtils.parseJWT(token);
                String roles = (String) claims.get("roles");
                if(StringUtils.equals(roles, MemberRoleEnum.ROLE_ADMIN.getRoleName())){
                    request.setAttribute(MemberRoleEnum.ROLE_ADMIN.getRoleAttrKey(), claims);
                }
                if(StringUtils.equals(roles, MemberRoleEnum.ROLE_USER.getRoleName())){
                    request.setAttribute(MemberRoleEnum.ROLE_USER.getRoleAttrKey(), claims);
                }
            }catch (Exception e){
                throw new JwtAuthException();
            }
        }

        return true;
    }


}
