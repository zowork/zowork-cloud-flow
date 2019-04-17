package com.zowork.cloud.flow.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记一个流程服务，标记一个Flow的服务
 * 
 * @author luolishu
 *
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FlowService {
	/**
	 * 命名空间
	 * 
	 * @return
	 */
	String namespace() default "";

	/**
	 * 业务分组
	 * @return
	 */
	String group() default "";
}
