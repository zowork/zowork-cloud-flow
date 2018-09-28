package com.zowork.cloud.flow.trace;

import java.io.Serializable;
import java.util.Map;

/**
 * flow的执行流程记录，方便图形展示
 * 
 * @author luolishu
 *
 */
public class NodeTrace implements Serializable {
	String id;
	String description;
	Map<String, Object> parameters;
	Map<String, Object> attributes;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

}
