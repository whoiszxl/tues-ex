package com.whoiszxl.tues.common.utils;

import org.springframework.cglib.beans.BeanCopier;

import java.util.HashMap;
import java.util.Map;

/**
 * Bean拷贝工具类
 *
 * @author zhouxiaolong
 * @date 2021/3/17
 */
public class BeanCopierUtils {

    //缓存map
    static Map<String, BeanCopier> beanCopierCacheMap = new HashMap<>();

    /**
     * 将对象从source拷贝到target中
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copyProperties(Object source, Object target) {
        //获取缓存名称
        String cacheKey = source.getClass().toString() + target.getClass().toString();

        BeanCopier beanCopier = null;
        if(!beanCopierCacheMap.containsKey(cacheKey)) {
            synchronized (BeanCopierUtils.class) {
                if(!beanCopierCacheMap.containsKey(cacheKey)) {
                    beanCopier = BeanCopier.create(source.getClass(), target.getClass(), false);
                    beanCopierCacheMap.put(cacheKey, beanCopier);
                }else{
                    beanCopier = beanCopierCacheMap.get(cacheKey);
                }
            }
        }else{
            beanCopier = beanCopierCacheMap.get(cacheKey);
        }
        beanCopier.copy(source, target, null);
    }

    private BeanCopierUtils() {}

}
