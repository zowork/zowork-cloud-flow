package com.zowork.cloud.flow.test.framework;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;

import com.zowork.cloud.flow.resolver.DefaultMethodResolver;
import com.zowork.cloud.flow.resolver.MethodResolver;
import com.zowork.cloud.flow.test.node.AnnotaionValidationNode;
import com.zowork.cloud.flow.test.node.DemoNode;
import com.zowork.cloud.flow.test.node.ValidationNode;

public class MethodResolverTest {
	@Test
	public void testValidate() {
		MethodResolver resolver = new DefaultMethodResolver();
		ValidationNode validateNode = new ValidationNode();
		Method method = resolver.resolveValidate(validateNode);
		Assert.assertNotNull(method);
	}
	@Test
	public void testValidateAnnotation() {
		MethodResolver resolver = new DefaultMethodResolver();
		AnnotaionValidationNode validateNode = new AnnotaionValidationNode();
		Method method = resolver.resolveValidate(validateNode);
		Assert.assertNotNull(method);
	}
	
	@Test
	public void testMethod() {
		MethodResolver resolver = new DefaultMethodResolver();
		DemoNode methodNode = new DemoNode();
		Method method = resolver.resolveAction(methodNode);
		Assert.assertNotNull(method);
	}
	
}
