package com.zowork.cloud.flow.node;

import com.zowork.cloud.flow.FlowConfiguration;

public class FlowScriptTagNode extends BaseTagNode implements FlowElement, Executable {
	private static final long serialVersionUID = 9045727731352955615L;
	String script;

	public FlowScriptTagNode(FlowConfiguration configuration, String script) {
		super();
		this.configuration = configuration;
		this.script = script;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

}
