package com.zowork.cloud.flow.test.param;

import java.io.Serializable;

public class PersonParam implements Serializable {
	Integer age;
	String city;
	Color color;// 0.未知 1.黄种人 2.白种人 3.黑种人

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public static enum Color {
		BLACK, WHITE, YELLOW, NONE
	}

}
