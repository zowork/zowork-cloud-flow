package com.zowork.cloud.flow.javascript;

import java.io.Serializable;
import java.util.Map;

public class ExecuteContext implements Serializable{
	String script;
	Map<String,Object> contextMap;

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public Map<String, Object> getContextMap() {
		return contextMap;
	}

	public void setContextMap(Map<String, Object> contextMap) {
		this.contextMap = contextMap;
	}
	

}
