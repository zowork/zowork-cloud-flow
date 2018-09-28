package com.zowork.cloud.flow.javascript;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;

import com.zowork.cloud.flow.FlowContext;

public class DefaultFlowScriptExecutor implements FlowScriptExecutor {

	@Override
	public Object execute(String script, FlowContext context) throws Exception {
		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("JavaScript");
		SimpleBindings bindings = this.getBindings(context);

		return engine.eval(script, bindings);

	}

	SimpleBindings getBindings(FlowContext context) {
		Map<String, Object> paramsMap = null;
		if (context.getContextMap() != null) {
			paramsMap = context.getContextMap();
		} else {
			paramsMap = new LinkedHashMap<>();
		}
		SimpleBindings bindings = new SimpleBindings(paramsMap);
		return bindings;
	}

}
