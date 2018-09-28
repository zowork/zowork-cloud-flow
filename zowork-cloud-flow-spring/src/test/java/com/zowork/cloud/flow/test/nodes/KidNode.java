package com.zowork.cloud.flow.test.nodes;

import com.zowork.cloud.flow.test.model.PersonModel;

public class KidNode {

	PersonModel execute() {
		System.out.println("execute====================="+this.getClass());
		PersonModel model = new PersonModel();
		model.setName("kid");
		model.setRegion("未知区域");
		return model;
	}
}
