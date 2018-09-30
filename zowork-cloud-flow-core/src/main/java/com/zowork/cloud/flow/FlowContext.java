package com.zowork.cloud.flow;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.zowork.cloud.flow.invocation.FlowInvocation;
import com.zowork.cloud.flow.node.FlowElement;
import com.zowork.cloud.flow.node.FlowTagNode;

@SuppressWarnings("rawtypes")
public class FlowContext {
	static final ThreadLocal<FlowContext> threadLocal = new ThreadLocal<FlowContext>();
	/**
	 * 变量内容，将方法的请求参数放进来
	 */
	final Map<String, Object> parametersMap = new FlowParameterMap();
	/**
	 * 将对象放到attributes中
	 */
	final Map<String, Object> attributesMap = new FlowAttributeMap();
	/**
	 * 执行历史
	 */
	final Map<String, FlowElement> historyMap = new LinkedHashMap<>();
	/**
	 * ognl 执行表达式的Map
	 */
	Map<String, Object> contextMap = null;
	/**
	 * 当前流程的ID
	 */
	String namespace;
	/**
	 * 流程ID
	 */
	String flowId;
	/**
	 * 流程节点
	 */
	FlowTagNode flowNode;
	/**
	 * 流程调用Invocation
	 */
	FlowInvocation invocation;
	/**
	 * 返回的服务类
	 */
	Class serviceInterface;
	/**
	 * 返回的结果类
	 */
	Class resultClass;
	/**
	 * 执行节点栈
	 */
	List<FlowElement> nodeStack;
	/**
	 * 抛异常的Node
	 */
	FlowElement currentNode;
	/**
	 * 执行的下一个节点
	 */
	FlowElement nextNode;
	/**
	 * 抛异常的Node
	 */
	FlowElement exceptionNode;
	/**
	 * flow的service方法参数
	 */
	Object methodArgs[];

	public FlowContext() {
		FlowContext.setContext(this);
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public FlowInvocation getInvocation() {
		return invocation;
	}

	public void setInvocation(FlowInvocation invocation) {
		this.invocation = invocation;
	}

	public FlowTagNode getFlowNode() {
		return flowNode;
	}

	public void setFlowNode(FlowTagNode flowNode) {
		this.flowNode = flowNode;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public static FlowContext getContext() {
		return threadLocal.get();
	}

	public static void setContext(FlowContext context) {
		threadLocal.set(context);
	}

	public Map<String, Object> getAttributesMap() {
		return attributesMap;
	}

	public FlowElement getNextNode() {
		return nextNode;
	}

	public void setNextNode(FlowElement nextNode) {
		this.nextNode = nextNode;
	}

	public FlowElement getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(FlowElement currentNode) {
		this.currentNode = currentNode;
	}

	public void setAttribute(String key, Object value) {
		attributesMap.put(key, value);
	}

	public Map<String, Object> getContextMap() {
		if (contextMap == null) {
			contextMap = new FlowContextMap(this);
		}
		contextMap.putAll(this.parametersMap);
		return contextMap;
	}

	public Object[] getMethodArgs() {
		return methodArgs;
	}

	public void setMethodArgs(Object[] methodArgs) {
		this.methodArgs = methodArgs;
	}

	public void setContextMap(Map<String, Object> contextMap) {
		if (this.contextMap == null) {
			this.contextMap = contextMap;
		} else {
			this.contextMap.putAll(contextMap);
		}

	}

	public void setResultClass(Class resultClass) {
		this.resultClass = resultClass;
	}

	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String key) {
		return (T) attributesMap.get(key);
	}

	public FlowElement getHistory(String id) {
		return historyMap.get(id);
	}

	public void setParameter(String key, Object value) {
		parametersMap.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T getParameter(String key) {
		return (T) parametersMap.get(key);
	}

	public Map<String, Object> getParametersMap() {
		return parametersMap;
	}

	public List<FlowElement> getNodeStack() {
		return nodeStack;
	}

	public void setNodeStack(List<FlowElement> nodeStack) {
		this.nodeStack = nodeStack;
	}

	public void addNode(FlowElement node) {
		if (this.nodeStack == null) {
			this.nodeStack = new LinkedList<>();
		}
		this.nodeStack.add(node);
		this.historyMap.put(node.getId(), node);
	}

	public Class getServiceInterface() {
		return serviceInterface;
	}

	public void setServiceInterface(Class serviceInterface) {
		this.serviceInterface = serviceInterface;
	}

	@SuppressWarnings("unchecked")
	public <T> Class<T> getResultClass() {
		return resultClass;
	}

	public FlowElement getExceptionNode() {
		return exceptionNode;
	}

	public void setExceptionNode(FlowElement exceptionNode) {
		this.exceptionNode = exceptionNode;
	}

	public Map<String, FlowElement> getHistoryMap() {
		return historyMap;
	}

	public static void cleanup() {
		threadLocal.set(null);
		threadLocal.remove();
	}
}
