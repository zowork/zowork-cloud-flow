package com.zowork.cloud.flow;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import com.zowork.cloud.flow.converter.DefaultFlowTypeConverter;
import com.zowork.cloud.flow.converter.FlowTypeConverter;
import com.zowork.cloud.flow.engine.DefaultFlowEngine;
import com.zowork.cloud.flow.engine.FlowEngineManager;
import com.zowork.cloud.flow.factory.DefaultFlowObjectFactory;
import com.zowork.cloud.flow.factory.FlowObjectFactory;
import com.zowork.cloud.flow.javascript.DefaultFlowScriptExecutor;
import com.zowork.cloud.flow.javascript.FlowScriptExecutor;
import com.zowork.cloud.flow.node.FlowActionTagNode;
import com.zowork.cloud.flow.node.FlowElement;
import com.zowork.cloud.flow.node.FlowTagNode;
import com.zowork.cloud.flow.resolver.ArgumentsResolver;
import com.zowork.cloud.flow.resolver.DefaultArgumentsResolver;
import com.zowork.cloud.flow.resolver.DefaultMethodResolver;
import com.zowork.cloud.flow.resolver.DefaultNodeAutowiredResolver;
import com.zowork.cloud.flow.resolver.ExpressionValueResolver;
import com.zowork.cloud.flow.resolver.MethodResolver;
import com.zowork.cloud.flow.resolver.NodeAutowiredResolver;
import com.zowork.cloud.flow.resolver.OgnlExpressionValueResolver;
import com.zowork.cloud.flow.result.DefaultFlowResultHandler;
import com.zowork.cloud.flow.result.FlowResultHandler;

public class FlowConfiguration {
	static final int MAX_STACK_SIZE = 2000;
	MethodResolver methodResolver = new DefaultMethodResolver();
	FlowObjectFactory objectFactory = new DefaultFlowObjectFactory();
	NodeAutowiredResolver autowiredResolver = new DefaultNodeAutowiredResolver(this);
	ArgumentsResolver argumentsResolver = new DefaultArgumentsResolver(this);
	FlowTypeConverter converter = new DefaultFlowTypeConverter();
	FlowScriptExecutor scriptExecutor = new DefaultFlowScriptExecutor();
	FlowResultHandler flowResultHandler = new DefaultFlowResultHandler(this);

	final Map<String, FlowTagNode> FLOWS_MAP = new ConcurrentHashMap<>();
	final Map<String, FlowElement> FLOW_NODES_MAP = new ConcurrentHashMap<>();
	ExpressionValueResolver expressionValueResolver = new OgnlExpressionValueResolver();
	FlowEngineManager flowEngineManager = new FlowEngineManager();

	public MethodResolver getMethodResolver() {
		return methodResolver;
	}

	public void setMethodResolver(MethodResolver resolver) {
		this.methodResolver = resolver;
	}

	public NodeAutowiredResolver getAutowiredResolver() {
		return autowiredResolver;
	}

	public void setAutowiredResolver(NodeAutowiredResolver autowiredResolver) {
		this.autowiredResolver = autowiredResolver;
	}

	public ArgumentsResolver getArgumentsResolver() {
		return argumentsResolver;
	}

	public void setArgumentsResolver(ArgumentsResolver argumentsResolver) {
		this.argumentsResolver = argumentsResolver;
	}

	public FlowTypeConverter getConverter() {
		return converter;
	}

	public void setConverter(FlowTypeConverter converter) {
		this.converter = converter;
	}

	public FlowTagNode getFlow(String flowId) {
		return FLOWS_MAP.get(flowId);
	}

	public ExpressionValueResolver getExpressionValueResolver() {
		return expressionValueResolver;
	}

	public void setExpressionValueResolver(ExpressionValueResolver expressionValueResolver) {
		this.expressionValueResolver = expressionValueResolver;
	}

	public FlowTypeConverter getTypeConverter() {
		return converter;
	}

	public MethodResolver getMethodResover() {
		return methodResolver;
	}

	public FlowObjectFactory getObjectFactory() {
		return objectFactory;
	}

	public void setObjectFactory(FlowObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
	}

	public void registerFlow(String namespace, String flowId, FlowTagNode flow) {
		String flowName = FlowUtils.getFlowName(namespace, flowId);
		FLOWS_MAP.put(flowName, flow);
		DefaultFlowEngine<?> engine = new DefaultFlowEngine<>(flow, this);
		this.getFlowEngineManager().register(namespace, flowId, engine);
	}

	public FlowEngineManager getFlowEngineManager() {
		return flowEngineManager;
	}

	public FlowScriptExecutor getScriptExecutor() {
		return scriptExecutor;
	}

	public void setScriptExecutor(FlowScriptExecutor scriptExecutor) {
		this.scriptExecutor = scriptExecutor;
	}

	public void registry(String namespace, String flowId, String id, FlowElement node) {
		String key = namespace + "_" + flowId + "_" + id;
		if (FLOW_NODES_MAP.containsKey(key)) {
			throw new FlowException("5000",
					"duplicate node exception!namespace=[" + namespace + "] flowId=[" + flowId + "] id=" + id);
		}
		FLOW_NODES_MAP.put(key, node);
	}

	public FlowElement getNodeById(String namespace, String flowId, String id) {
		String key = namespace + "_" + flowId + "_" + id;
		return FLOW_NODES_MAP.get(key);
	}

	public int getMaxFlowStackSize() {
		return MAX_STACK_SIZE;
	}

	public List<FlowElement> getActionNodes() {
		if (CollectionUtils.isEmpty(FLOW_NODES_MAP.values())) {
			return Collections.emptyList();
		}
		return FLOW_NODES_MAP.values().stream().filter((item) -> {
			return item instanceof FlowActionTagNode;
		}).collect(Collectors.toList());
	}

	public FlowResultHandler getFlowResultHandler() {
		return flowResultHandler;
	}

	public void setFlowResultHandler(FlowResultHandler flowResultHandler) {
		this.flowResultHandler = flowResultHandler;
	}

	public void removeFlow(String namespace, String flowId) {
		String flowName = FlowUtils.getFlowName(namespace, flowId);
		FLOWS_MAP.remove(flowName);
		this.getFlowEngineManager().remove(namespace, flowId);
	}

}
