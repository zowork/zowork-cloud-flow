package com.zowork.cloud.flow.converter;

import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.TypeConverter;

public class DefaultFlowTypeConverter implements FlowTypeConverter {
	static TypeConverter typeConverter = new SimpleTypeConverter();

	@Override
	public <T> T convertIfNecessary(Object value, Class<T> requiredType) {
		value = typeConverter.convertIfNecessary(value, requiredType);
		return (T) value;
	}

}
