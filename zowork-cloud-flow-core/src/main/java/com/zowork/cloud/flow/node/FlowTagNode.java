package com.zowork.cloud.flow.node;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.zowork.cloud.flow.FlowConfiguration;

public class FlowTagNode extends BaseTagNode implements FlowElement {
	private static final long serialVersionUID = -6055917811795357850L;
	String serviceClass;
	final FlowElement first;
	final FlowElement last;
	final List<FlowElement> nodeList;
	Class<?> resultClass;

	public FlowTagNode(String id, List<FlowElement> nodeList, FlowConfiguration configuration) {
		super();
		this.id = id;
		this.nodeList = nodeList;
		this.configuration = configuration;
		if (CollectionUtils.isNotEmpty(nodeList)) {
			first = nodeList.get(0);
			last = nodeList.get(nodeList.size() - 1);
		} else {
			first = null;
			last = null;
		}
	}

	public FlowElement getFirst() {
		return first;
	}

	public FlowElement getLast() {
		return last;
	}

	public FlowElement getNodeById(String id) {
		return configuration.getNodeById(this.getNamespace(), this.getId(), id);
	}

	public List<FlowElement> getNodeList() {
		return nodeList;
	}

	public Class<?> getResultClass() {
		return resultClass;
	}

	public void setResultClass(Class<?> resultClass) {
		this.resultClass = resultClass;
	}

	public String getServiceClass() {
		return serviceClass;
	}

	public void setServiceClass(String serviceClass) {
		this.serviceClass = serviceClass;
	}

}
