package com.zowork.cloud.flow.test.node;

import com.zowork.cloud.flow.annotation.FlowAction;
import com.zowork.cloud.flow.annotation.FlowParam;

public class AnnotationDemoNode {
	@FlowAction
	public void testExecute(@FlowParam("orderId") Long orderId) {

	}

}
