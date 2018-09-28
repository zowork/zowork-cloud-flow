package com.zowork.cloud.flow.test.nodes.inject;

import com.zowork.cloud.flow.annotation.FlowAttribute;

public class AttributeInjectNode {

	public void execute(@FlowAttribute("age") Integer age) {
		System.out.println("age===================" + age);

	}
}
