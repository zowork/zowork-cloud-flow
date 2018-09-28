package com.zowork.cloud.flow.test.nodes.inject;

import com.zowork.cloud.flow.annotation.FlowAttribute;
import com.zowork.cloud.flow.test.model.PersonModel;

public class AttributeResultNode {
	@FlowAttribute("person")
	public Object execute(@FlowAttribute("age") Integer age) {
	 

		PersonModel person = new PersonModel();
		person.setName("attribute node");
		
		return person;
	}
}
