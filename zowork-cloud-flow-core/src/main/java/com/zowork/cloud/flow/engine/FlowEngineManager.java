package com.zowork.cloud.flow.engine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.zowork.cloud.flow.FlowException;
import com.zowork.cloud.flow.FlowUtils;

public class FlowEngineManager {

	static Map<String, FlowEngine<?>> flowMap = new ConcurrentHashMap<>();

	@SuppressWarnings("unchecked")
	public <T> FlowEngine<T> getEngine(String namespace, String flowId) {

		String flowName = FlowUtils.getFlowName(namespace, flowId);
		if (!flowMap.containsKey(flowName)) {
			throw new FlowException("5000", "flow not found!namespace="+namespace+",flowId="+flowId);
		}
		return (FlowEngine<T>) flowMap.get(flowName);
	}

	public void register(String namespace, String flowId, FlowEngine<?> engine) {
		String flowName = FlowUtils.getFlowName(namespace, flowId);
		flowMap.put(flowName, engine);
	}

	public void remove(String namespace, String flowId) {
		String flowName = FlowUtils.getFlowName(namespace, flowId);
		flowMap.remove(flowName);
	}
}
