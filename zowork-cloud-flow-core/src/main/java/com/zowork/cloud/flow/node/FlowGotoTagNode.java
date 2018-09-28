package com.zowork.cloud.flow.node;

import com.zowork.cloud.flow.FlowConfiguration;

public class FlowGotoTagNode extends BaseTagNode implements FlowElement, Executable {
	private static final long serialVersionUID = 4470541043389415539L;
	String id;
	String flowId;

	public FlowGotoTagNode(FlowConfiguration configuration, String id, String flowId) {
		super();
		this.configuration = configuration;
		this.id = id;
		this.flowId = flowId;
		this.test = "true";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

}
