package com.zowork.cloud.flow.resolver;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.zowork.cloud.flow.FlowConfiguration;
import com.zowork.cloud.flow.FlowContext;
import com.zowork.cloud.flow.FlowContextMap;

public class ExpressionValueResolverTest {
	@Test
	public void testAttribute() {
		FlowConfiguration configuration = new FlowConfiguration();
		FlowContext context = new FlowContext();
		context.getAttributesMap().put("attr", "hell attr!");
		context.setParameter("param", "hello param");
		context.setAttribute("attr2", 1);
		FlowContextMap contextMap = new FlowContextMap(context);
		boolean result = configuration.getExpressionValueResolver().resolveBoolean("$Attr.attr=='hell attr!'", contextMap);
		Assert.assertTrue(result);
		result = configuration.getExpressionValueResolver().resolveBoolean("$Attr.attr2==1", contextMap);
		Assert.assertTrue(result);
	}

	@Test
	public void testParam() {
		FlowConfiguration configuration = new FlowConfiguration();
		FlowContext context = new FlowContext();
		context.getAttributesMap().put("attr", "hell attr!");
		context.setParameter("param", "hello param");
		context.setParameter("param2", 2);
		FlowContextMap contextMap = new FlowContextMap(context);
		boolean result = configuration.getExpressionValueResolver().resolveBoolean("$Param.param=='hello param'", contextMap);
		Assert.assertTrue(result);
		result = configuration.getExpressionValueResolver().resolveBoolean("$Param.param2==2", contextMap);
		Assert.assertTrue(result);
	}

	@Test
	public void testRootMap() {
		FlowConfiguration configuration = new FlowConfiguration();
		FlowContext context = new FlowContext();
		context.getAttributesMap().put("attr", "hell attr!");
		context.setParameter("param", "hello param");
		Map<String, Object> rootMap = new LinkedHashMap<>();
		rootMap.put("helloworld", 2);

		FlowContextMap contextMap = new FlowContextMap(context);
		contextMap.addArgument(rootMap);
		boolean result = configuration.getExpressionValueResolver().resolveBoolean("helloworld==2", contextMap);
		Assert.assertTrue(result);
	}

	@Test
	public void testRootObject() {
		FlowConfiguration configuration = new FlowConfiguration();
		FlowContext context = new FlowContext();
		context.getAttributesMap().put("attr", "hell attr!");
		context.setParameter("param", "hello param");
		Map<String, Object> rootMap = new LinkedHashMap<>();
		rootMap.put("helloworld", 2);
		Person person = new Person();
		person.setAge(18);

		Person person2 = new Person();
		person2.setAge(19);

		FlowContextMap contextMap = new FlowContextMap(context);
		contextMap.addArgument(rootMap);
		contextMap.addArgument(person);
		contextMap.addArgument(person2);
		boolean result = configuration.getExpressionValueResolver().resolveBoolean("helloworld==2", contextMap);
		Assert.assertTrue(result);
		result = configuration.getExpressionValueResolver().resolveBoolean("age==18", contextMap);
		Assert.assertTrue(result);
	}
	@Test
	public void testTrue() {
		FlowConfiguration configuration = new FlowConfiguration();
		FlowContext context = new FlowContext();
		context.getAttributesMap().put("attr", "hell attr!");
		context.setParameter("param", "hello param");
		Map<String, Object> rootMap = new LinkedHashMap<>();
		rootMap.put("helloworld", 2);
		Person person = new Person();
		person.setAge(18);

		Person person2 = new Person();
		person2.setAge(19);

		FlowContextMap contextMap = new FlowContextMap(context);
		contextMap.addArgument(rootMap);
		contextMap.addArgument(person);
		contextMap.addArgument(person2);
		boolean result = configuration.getExpressionValueResolver().resolveBoolean("true==true", contextMap);
		Assert.assertTrue(result);
		result = configuration.getExpressionValueResolver().resolveBoolean("true", contextMap);
		Assert.assertTrue(result);
	}


	public static class Person {
		Integer age;

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}

	}
}
