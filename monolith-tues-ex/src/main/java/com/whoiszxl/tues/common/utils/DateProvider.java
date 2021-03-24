package com.whoiszxl.tues.common.utils;

import java.time.LocalDateTime;

/**
 * 日期服务
 */
public interface DateProvider {

	/**
	 * 获取当前时间
	 * @return
	 */
	LocalDateTime now();
	
}
