package com.zowork.cloud.flow.javascript;

import com.zowork.cloud.flow.FlowContext;

public interface FlowScriptExecutor {

	Object execute(String script,FlowContext context) throws Exception;

}
