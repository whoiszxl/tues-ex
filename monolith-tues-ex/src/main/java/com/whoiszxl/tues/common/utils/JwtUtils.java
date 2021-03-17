package com.whoiszxl.tues.common.utils;

import com.whoiszxl.tues.common.enums.MemberRoleEnum;
import com.whoiszxl.tues.common.exception.ExceptionCatcher;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * jwt工具类
 *
 * @author zhouxiaolong
 * @date 2021/3/17
 */
@Data
@Component
@ConfigurationProperties("jwt.config")
public class JwtUtils {
    private String key;
    private long ttl;

    /**
     * 创建jwt
     * @param id
     * @param subject
     * @param roles
     * @return
     */
    public String createJWT(String id, String subject, String roles) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder().setId(id)
                .setSubject(subject) //jwt面向的用户
                .setIssuedAt(now) //设置签发时间
                .signWith(SignatureAlgorithm.HS256, key) //设置签名方式
                .claim("roles", roles); //设置角色
        if(ttl > 0) {
            builder.setExpiration(new Date(nowMillis + ttl));
        }
        return builder.compact();
    }

    /**
     * 解析JWT
     * @param jwtStr
     * @return
     */
    public Claims parseJWT(String jwtStr){
        return  Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwtStr)
                .getBody();
    }


    public static Claims getClaims(MemberRoleEnum userRoleEnum, HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute(userRoleEnum.getRoleAttrKey());
        if(claims == null) {
            ExceptionCatcher.catchJwtAuthEx();
        }
        return claims;
    }

    public static Claims getUserClaims(HttpServletRequest request) {
        return getClaims(MemberRoleEnum.ROLE_USER, request);
    }

    public static  Claims getAdminClaims(HttpServletRequest request) {
        return getClaims(MemberRoleEnum.ROLE_ADMIN, request);
    }
}
