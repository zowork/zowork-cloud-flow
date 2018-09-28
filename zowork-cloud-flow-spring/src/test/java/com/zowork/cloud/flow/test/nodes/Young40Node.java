package com.zowork.cloud.flow.test.nodes;

import com.zowork.cloud.flow.test.model.PersonModel;

public class Young40Node {
	PersonModel execute() {
		System.out.println("execute=====================" + this.getClass());
		PersonModel model = new PersonModel();
		model.setName("young");
		model.setGroup("不惑");
		return model;
	}
}
