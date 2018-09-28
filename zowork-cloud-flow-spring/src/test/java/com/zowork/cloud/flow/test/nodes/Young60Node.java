package com.zowork.cloud.flow.test.nodes;

import com.zowork.cloud.flow.test.model.PersonModel;

public class Young60Node {
	PersonModel execute() {
		System.out.println("execute=====================" + this.getClass());
		PersonModel model = new PersonModel();
		model.setName("young");
		model.setGroup("耳顺");
		return model;
	}
}
