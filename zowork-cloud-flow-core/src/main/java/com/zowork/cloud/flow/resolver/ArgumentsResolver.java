package com.zowork.cloud.flow.resolver;

import java.lang.reflect.Method;

public interface ArgumentsResolver {
	Object[] resolveMethodArguments(Method handlerMethod, Object action) throws Exception;
}
