
package com.zowork.cloud.flow.resolver;

/**
 * 获取表达式的值
 * 
 * @author luolishu
 *
 */
public interface ExpressionValueResolver {

	boolean resolveBoolean(String expression, Object parameterObject);

	Object resolveObject(String expression, Object parameterObject);

	Iterable<?> resolveIterable(String expression, Object parameterObject);

}
