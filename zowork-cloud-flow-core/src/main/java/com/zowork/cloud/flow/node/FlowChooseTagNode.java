package com.zowork.cloud.flow.node;

import java.util.List;

import com.zowork.cloud.flow.FlowConfiguration;

public class FlowChooseTagNode extends BaseTagNode implements FlowElement {
	private static final long serialVersionUID = -65726418741416246L;
	FlowElement defaultNode;
	List<FlowElement> ifNodeList;
	String description;

	public FlowChooseTagNode(FlowConfiguration configuration, List<FlowElement> ifNodeList, FlowElement defaultNode) {
		this.configuration = configuration;
		this.ifNodeList = ifNodeList;
		this.defaultNode = defaultNode;
		this.setTest("true");
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public FlowElement getDefaultNode() {
		return defaultNode;
	}

	public void setDefaultNode(FlowElement defaultNode) {
		this.defaultNode = defaultNode;
	}

	public List<FlowElement> getIfNodeList() {
		return ifNodeList;
	}

	public void setIfNodeList(List<FlowElement> ifNodeList) {
		this.ifNodeList = ifNodeList;
	}

}
