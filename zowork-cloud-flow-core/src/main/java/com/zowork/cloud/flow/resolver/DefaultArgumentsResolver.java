package com.zowork.cloud.flow.resolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.SynthesizingMethodParameter;

import com.zowork.cloud.flow.FlowConfiguration;
import com.zowork.cloud.flow.FlowContext;
import com.zowork.cloud.flow.FlowUtils;
import com.zowork.cloud.flow.annotation.FlowAttribute;
import com.zowork.cloud.flow.annotation.FlowParam;
import com.zowork.cloud.flow.converter.FlowTypeConverter;

public class DefaultArgumentsResolver implements ArgumentsResolver {
	FlowConfiguration configuration;
	static DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
	static Set<Class<?>> classSet = new HashSet<Class<?>>();
	static {
		classSet.add(FlowContext.class);
	}

	public DefaultArgumentsResolver(FlowConfiguration configuration) {
		super();
		this.configuration = configuration;
	}

	public FlowConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(FlowConfiguration configuration) {
		this.configuration = configuration;
	}

	public Object[] resolveMethodArguments(Method handlerMethod, Object action) throws Exception {

		Class<?>[] paramTypes = handlerMethod.getParameterTypes();
		Object[] args = new Object[paramTypes.length];
		for (int i = 0; i < args.length; i++) {
			Class<?> parameterType = paramTypes[i];
			if (FlowContext.class.isAssignableFrom(parameterType)) {
				args[i] = FlowContext.getContext();
				continue;
			}
			MethodParameter methodParam = new SynthesizingMethodParameter(handlerMethod, i);
			methodParam.initParameterNameDiscovery(parameterNameDiscoverer);
			GenericTypeResolver.resolveParameterType(methodParam, action.getClass());
			Annotation[] paramAnns = methodParam.getParameterAnnotations();
			for (Annotation paramAnn : paramAnns) {
				if (FlowParam.class.isInstance(paramAnn)) {
					args[i] = resolveFlowParam((FlowParam) paramAnn, methodParam);
				}
				if (FlowAttribute.class.isInstance(paramAnn)) {
					args[i] = resolveFlowAttribute((FlowAttribute) paramAnn, methodParam);
				}
			}
			if (args[i] == null) {
				Object[] methodArgs = FlowContext.getContext().getMethodArgs();
				if (methodArgs == null) {
					continue;
				}
				for (Object methodArg : methodArgs) {
					if (methodArg == null) {
						continue;
					}
					if (parameterType.isAssignableFrom(methodArg.getClass())
							&& classSet.contains(methodArg.getClass())) {
						args[i] = methodArg;
						break;
					}
				}
			}
		}
		return args;
	}

	private Object resolveFlowParam(FlowParam paramAnn, MethodParameter methodParam) throws Exception {
		return FlowUtils.resolveParameter(paramAnn, methodParam, configuration);

	}

	private Object resolveFlowAttribute(FlowAttribute attrAnn, MethodParameter methodParam) throws Exception {
		return FlowUtils.resolveAttribute(attrAnn, methodParam, configuration);

	}

	FlowTypeConverter getTypeConverter() {
		return configuration.getTypeConverter();
	}

	public static void main(String args[]) {
		System.out.println(Map.class.isAssignableFrom(HashMap.class));
		System.out.println(HashMap.class.isAssignableFrom(Map.class));
	}
}
