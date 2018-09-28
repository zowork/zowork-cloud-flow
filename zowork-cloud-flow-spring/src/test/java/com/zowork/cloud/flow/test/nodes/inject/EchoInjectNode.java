package com.zowork.cloud.flow.test.nodes.inject;

import java.util.Map;

import com.zowork.cloud.flow.annotation.FlowAction;
import com.zowork.cloud.flow.annotation.FlowAttribute;
import com.zowork.cloud.flow.annotation.FlowParam;
import com.zowork.cloud.flow.test.model.PersonModel;
import com.zowork.cloud.flow.test.param.PersonParam;

public class EchoInjectNode {
	@FlowParam("city")
	String city2;

	@FlowAction
	public void executeXXX(@FlowParam("age") Integer age, @FlowAttribute("person") PersonModel person,
			@FlowAttribute("person.name") String name, PersonParam param,Map param2, @FlowParam("city") String city) {
		System.out.println("==============age================" + age);
		System.out.println("==============person================" + person);
		System.out.println("==============person.name================" + name);
		System.out.println("==============param================" + param);
		System.out.println("==============paramMap================" + param2);
		System.out.println("==============city================" + city);
		System.out.println("==============city2================" + city2);
	}
}
