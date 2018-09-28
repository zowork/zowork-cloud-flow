package com.zowork.cloud.flow.node;

import java.util.List;

import com.zowork.cloud.flow.FlowConfiguration;

public class FlowElseIfTagNode extends BaseTagNode implements FlowElement {
	private static final long serialVersionUID = 4546619693268667029L;

	public FlowElseIfTagNode(FlowConfiguration configuration, String test, List<FlowElement> nodeList) {
		this.childs.addAll(nodeList);
		this.test = test;
		this.configuration = configuration;
	}

}
