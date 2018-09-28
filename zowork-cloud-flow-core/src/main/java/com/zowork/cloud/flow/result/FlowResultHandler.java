package com.zowork.cloud.flow.result;

import java.lang.reflect.Method;

public interface FlowResultHandler {

	public Object handle(Method method, Object result);
}
