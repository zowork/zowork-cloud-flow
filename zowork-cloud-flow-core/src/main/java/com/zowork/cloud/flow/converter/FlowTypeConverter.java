package com.zowork.cloud.flow.converter;

public interface FlowTypeConverter {

	<T> T convertIfNecessary(Object value, Class<T> requiredType);
}
