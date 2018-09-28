package com.zowork.cloud.flow.config;

import com.zowork.cloud.flow.FlowConfiguration;
import com.zowork.cloud.flow.node.FlowElement;

public interface ParseContext {
	/**
	 * 获取配置信息
	 * 
	 * @return
	 */
	FlowConfiguration getConfiguration();

	/**
	 * 命名空间
	 * 
	 * @return
	 */
	String getNamespace();

	/**
	 * 当前的flowId
	 * 
	 * @return
	 */
	String getFlowId();

	/**
	 * 注册一个flow的节点
	 * 
	 * @param id
	 * @param node
	 */
	void registry(String id, FlowElement node);
}
