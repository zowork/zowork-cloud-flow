package com.zowork.cloud.flow.resolver;

import java.lang.reflect.Method;

public interface MethodResolver {

	Method resolveValidate(Object node);

	Method resolveAction(Object node);

}
