package com.zowork.cloud.flow.test.nodes;

import com.zowork.cloud.flow.test.model.PersonModel;

public class KidSouthNode {

	PersonModel execute() {
		System.out.println("execute====================="+this.getClass());
		PersonModel model = new PersonModel();
		model.setName("kid");
		model.setRegion("南方人");
		return model;
	}
}
