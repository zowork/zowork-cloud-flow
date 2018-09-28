package com.zowork.cloud.flow.node;

import java.util.List;

import com.zowork.cloud.flow.FlowConfiguration;

public class FlowIfTagNode extends BaseTagNode implements FlowElement {
	private static final long serialVersionUID = 4546619693268667029L;

	public FlowIfTagNode(FlowConfiguration configuration, String test, List<FlowElement> nodeList) {
		this.childs.addAll(nodeList);
		this.test = test;
		this.configuration = configuration;
	}

}
