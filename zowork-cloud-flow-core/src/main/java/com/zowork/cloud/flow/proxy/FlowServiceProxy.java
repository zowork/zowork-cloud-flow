package com.zowork.cloud.flow.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;

import com.zowork.cloud.flow.FlowConfiguration;
import com.zowork.cloud.flow.FlowContext;
import com.zowork.cloud.flow.FlowUtils;
import com.zowork.cloud.flow.annotation.FlowService;
import com.zowork.cloud.flow.annotation.FlowServiceMethod;
import com.zowork.cloud.flow.engine.FlowEngine;

public class FlowServiceProxy<T> implements InvocationHandler {
	@Resource
	FlowConfiguration configration;
	private final Class<T> serviceInterface;

	public FlowServiceProxy(Class<T> serviceInterface, FlowConfiguration configration) {
		super();
		this.serviceInterface = serviceInterface;
		this.configration = configration;
	}

	/**
	 * 代理类
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		FlowServiceMethod flowMethod = method.getAnnotation(FlowServiceMethod.class);
		FlowService flowService = AnnotationUtils.findAnnotation(proxy.getClass(), FlowService.class);
		String flowId = method.getName();

		if (flowMethod != null && StringUtils.isNotBlank(flowMethod.id())) {
			flowId = flowMethod.id();
		}
		String namespace = serviceInterface.getPackage().getName();
		if (flowService != null && StringUtils.isNotBlank(flowService.namespace())) {
			namespace = flowService.namespace();
		}
		FlowContext context = this.createContext(method, args);
		Object model = null;
		try {
			FlowEngine<?> flowEngine = configration.getFlowEngineManager().getEngine(namespace, flowId);
			model = flowEngine.process(context);
		} finally {
			FlowContext.cleanup();
		}
		return model;
	}

	FlowContext createContext(Method method, Object[] args) {

		FlowContext context = new FlowContext(); 
		Map<String, Object> contextMap = FlowUtils.resolveContextMap(method, args);
		context.setContextMap(contextMap);
		context.setServiceInterface(serviceInterface);
		context.setResultClass(method.getReturnType());
		context.setMethodArgs(args);
		return context;
	}

}
