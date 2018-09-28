package com.zowork.cloud.flow.trace;

import java.io.Serializable;
import java.util.List;

/**
 * flow的执行流程记录，方便图形展示
 * 
 * @author luolishu
 *
 */
public class FlowTrace implements Serializable {
	String id;
	List<NodeTrace> nodeList;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<NodeTrace> getNodeList() {
		return nodeList;
	}

	public void setNodeList(List<NodeTrace> nodeList) {
		this.nodeList = nodeList;
	}

}
