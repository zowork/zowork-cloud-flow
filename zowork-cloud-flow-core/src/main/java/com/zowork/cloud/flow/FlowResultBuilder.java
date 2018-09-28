/**
 * 
 */
package com.zowork.cloud.flow;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.zowork.cloud.flow.annotation.FlowAttribute;
import com.zowork.cloud.flow.annotation.FlowParam;
import com.zowork.cloud.flow.trace.FlowTrace;

/**
 * @author luolishu
 *
 */
public class FlowResultBuilder {

	public static <T extends Serializable> T buildResult(Class<T> resultClass, FlowConfiguration configuration) {
		if (resultClass == null) {
			return null;
		}
		if (Void.class.isAssignableFrom(resultClass) || resultClass == Void.TYPE.getClass()
				|| resultClass.getConstructors() == null || resultClass.getConstructors().length <= 0) {
			return null;
		}
		T result = null;
		try {
			result = resultClass.newInstance();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Field[] fieldArray = FieldUtils.getAllFields(resultClass);
		for (Field field : fieldArray) {
			Object value = resolveValue(field, configuration);
			if (value != null) {
				try {
					FieldUtils.writeField(field, result, value, true);
				} catch (IllegalAccessException e) {
					throw new FlowException("5000", e.getMessage());
				}
			}

		}
		return result;
	}

	static FlowTrace buildTrace(FlowContext context, FlowConfiguration configuration) {
		return null;
	}

	static Object resolveValue(Field field, FlowConfiguration configuration) {
		FlowContext context = FlowContext.getContext();
		FlowAttribute attrAnn = field.getAnnotation(FlowAttribute.class);
		Object value = null;
		if (attrAnn != null) {
			value = context.getAttribute(attrAnn.value()[0]);
		}
		if (value != null) {
			return value;
		}
		FlowParam paramAnn = field.getAnnotation(FlowParam.class);

		if (paramAnn != null) {
			value = context.getParameter(paramAnn.value()[0]);
		}
		if (value == null) {
			value = context.getAttribute(field.getName());
		}

		if (value == null) {
			value = context.getParameter(field.getName());
		}
		if (value == null) {
			value = configuration.getExpressionValueResolver().resolveObject(field.getName(), context.getContextMap());
		}
		if (FlowTrace.class.isAssignableFrom(field.getType())) {
			FlowTrace trace = buildTrace(context, configuration);
			return trace;
		}
		return value;

	}

}
