package com.zowork.cloud.flow.node;

import java.util.List;

import com.zowork.cloud.flow.FlowConfiguration;

public class FlowSubFlowTagNode extends BaseTagNode implements FlowElement {
	private static final long serialVersionUID = 1149033617069620269L;
	String refFlowId;

	public FlowSubFlowTagNode(FlowConfiguration configuration, String id, String refFlowId,
			List<FlowElement> nodeList) {
		super();
		this.configuration = configuration;
		this.id = id;
		this.refFlowId = refFlowId;
		this.childs.addAll(nodeList);
	}

	public String getRefFlowId() {
		return refFlowId;
	}

	public void setRefFlowId(String refFlowId) {
		this.refFlowId = refFlowId;
	}

}
