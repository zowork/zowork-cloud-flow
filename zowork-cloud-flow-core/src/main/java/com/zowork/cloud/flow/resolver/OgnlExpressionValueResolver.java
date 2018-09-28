package com.zowork.cloud.flow.resolver;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.scripting.xmltags.OgnlCache;

public class OgnlExpressionValueResolver implements ExpressionValueResolver {
	public boolean resolveBoolean(String expression, Object parameterObject) {
		Object value = OgnlCache.getValue(expression, parameterObject);
		if (value instanceof Boolean) {
			return (Boolean) value;
		}
		if (value instanceof Number) {
			BigDecimal decimal = new BigDecimal(String.valueOf(value));
			return !decimal.equals(BigDecimal.ZERO) && decimal.longValue() > 0;
		}
		return value != null;
	}

	public Object resolveObject(String expression, Object parameterObject) {
		Object value = OgnlCache.getValue(expression, parameterObject);
		return value;
	}

	@SuppressWarnings("rawtypes")
	public Iterable<?> resolveIterable(String expression, Object parameterObject) {
		Object value = OgnlCache.getValue(expression, parameterObject);
		if (value == null) {
			throw new BuilderException("The expression '" + expression + "' evaluated to a null value.");
		}
		if (value instanceof Iterable) {
			return (Iterable<?>) value;
		}
		if (value.getClass().isArray()) {
			// the array may be primitive, so Arrays.asList() may throw
			// a ClassCastException (issue 209). Do the work manually
			// Curse primitives! :) (JGB)
			int size = Array.getLength(value);
			List<Object> answer = new ArrayList<Object>();
			for (int i = 0; i < size; i++) {
				Object o = Array.get(value, i);
				answer.add(o);
			}
			return answer;
		}
		if (value instanceof Map) {
			return ((Map) value).entrySet();
		}
		throw new BuilderException(
				"Error evaluating expression '" + expression + "'.  Return value (" + value + ") was not iterable.");
	}
}
