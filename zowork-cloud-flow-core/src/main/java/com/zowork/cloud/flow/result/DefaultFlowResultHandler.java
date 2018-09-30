package com.zowork.cloud.flow.result;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;

import com.zowork.cloud.flow.FlowAttributeMap;
import com.zowork.cloud.flow.FlowConfiguration;
import com.zowork.cloud.flow.FlowContext;
import com.zowork.cloud.flow.annotation.FlowAttribute;

public class DefaultFlowResultHandler implements FlowResultHandler {
	FlowConfiguration configuration;

	public DefaultFlowResultHandler(FlowConfiguration configuration) {
		super();
		this.configuration = configuration;
	}

	@Override
	public Object handle(Method method, Object result) {
		FlowContext context = FlowContext.getContext();
		if (result == null) {
			return result;
		}
		if (result instanceof FlowAttributeMap) {
			context.getAttributesMap().putAll((FlowAttributeMap) result);
			return result;
		}
		FlowAttribute attributeAnn = AnnotationUtils.findAnnotation(method, FlowAttribute.class);
		if (attributeAnn != null) {
			for (String key : attributeAnn.value()) {
				if (StringUtils.isBlank(key)) {
					continue;
				}
				context.setAttribute(key, result);
			}
		}
		return result;
	}

}
