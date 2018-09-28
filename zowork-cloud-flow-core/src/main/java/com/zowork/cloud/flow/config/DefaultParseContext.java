package com.zowork.cloud.flow.config;

import com.zowork.cloud.flow.FlowConfiguration;
import com.zowork.cloud.flow.node.FlowElement;

public class DefaultParseContext implements ParseContext {
	final FlowConfiguration configuration;
	String namespace;
	String flowId;

	public DefaultParseContext(FlowConfiguration configuration, String namespace) {
		super();
		this.configuration = configuration;
		this.namespace = namespace;
	}

	@Override
	public FlowConfiguration getConfiguration() {
		return configuration;
	}

	@Override
	public String getNamespace() {
		return namespace;
	}

	@Override
	public String getFlowId() {
		return flowId;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	@Override
	public void registry(String id, FlowElement node) {
		configuration.registry(namespace, flowId, id, node);
	}

}
