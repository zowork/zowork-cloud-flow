package com.zowork.cloud.flow.service;

import java.lang.reflect.Method;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zowork.cloud.flow.FlowConfiguration;
import com.zowork.cloud.flow.FlowContext;
import com.zowork.cloud.flow.FlowUtils;
import com.zowork.cloud.flow.engine.FlowEngine;

@Service
public class DefaultProcessService<M, P> implements ProcessService<M, P> {
	@Resource
	FlowConfiguration configration;

	public DefaultProcessService(FlowConfiguration configration) {
		super();
		this.configration = configration;
	}

	/**
	 * 支付流程注入
	 */

	@Override
	public M process(String namespace, String flowId, P param) {
		FlowContext context = new FlowContext();
		M model = null;
		try {
			FlowEngine<M> flowEngine = configration.getFlowEngineManager().getEngine(namespace, flowId);
			model = flowEngine.process(context);
		} finally {
			FlowContext.cleanup();
		}
		return model;
	}

	FlowContext createContext(Method method, P param) {
		
		FlowContext context = new FlowContext();
		
		Map<String, Object> contextMap = FlowUtils.resolveServiceContextMap(method, param);
		context.setContextMap(contextMap); 
		context.setServiceInterface(ProcessService.class);
		context.setResultClass(method.getReturnType());

		return context;
	}

	public FlowConfiguration getConfigration() {
		return configration;
	}

	public void setConfigration(FlowConfiguration configration) {
		this.configration = configration;
	}

}
