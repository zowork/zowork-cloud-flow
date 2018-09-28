package com.zowork.cloud.flow.test.nodes.inject;

import com.zowork.cloud.flow.annotation.FlowParam;

public class ParameterInjectNode {
	public void execute(@FlowParam("age") Integer age) {
		 System.out.println("age============ParameterInjectNode======="+age);

	}
}
