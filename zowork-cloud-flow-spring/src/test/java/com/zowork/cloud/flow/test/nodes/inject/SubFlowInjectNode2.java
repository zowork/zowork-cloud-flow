package com.zowork.cloud.flow.test.nodes.inject;

import java.util.Map;

import com.zowork.cloud.flow.annotation.FlowAction;
import com.zowork.cloud.flow.annotation.FlowAttribute;
import com.zowork.cloud.flow.annotation.FlowParam;
import com.zowork.cloud.flow.test.model.PersonModel;
import com.zowork.cloud.flow.test.param.PersonParam;

public class SubFlowInjectNode2 {
	@FlowParam("city")
	String city2;

	@FlowAction
	public void executeXXX(@FlowParam("channel") String channel, @FlowParam("age") Integer age,
			@FlowAttribute("person") PersonModel person, @FlowAttribute("person.name") String name, PersonParam param,
			Map param2, @FlowParam("city") String city) {

		System.out.println("==============SubFlowInjectNode2================" + channel); 
	}
}
