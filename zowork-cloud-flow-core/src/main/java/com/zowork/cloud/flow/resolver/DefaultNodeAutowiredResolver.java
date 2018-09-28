package com.zowork.cloud.flow.resolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.zowork.cloud.flow.FlowConfiguration;
import com.zowork.cloud.flow.FlowUtils;
import com.zowork.cloud.flow.annotation.FlowAttribute;
import com.zowork.cloud.flow.annotation.FlowParam;

public class DefaultNodeAutowiredResolver implements NodeAutowiredResolver {
	FlowConfiguration configuration;

	public DefaultNodeAutowiredResolver(FlowConfiguration configuration) {
		super();
		this.configuration = configuration;
	}

	@Override
	public void resolve(Object nodeBean) {
		if (nodeBean == null) {
			return;
		}
		Field[] fields = FieldUtils.getAllFields(nodeBean.getClass());
		for (Field field : fields) {
			Annotation[] anns = field.getAnnotations();
			for (Annotation ann : anns) {
				if (ann instanceof FlowAttribute) {
					try {
						Object value = FlowUtils.resolveAttribute((FlowAttribute) ann, field.getType(), configuration);
						if (value != null) {
							if (!field.isAccessible()) {
								field.setAccessible(true);
							}
							field.set(nodeBean, value);
						}
					} catch (Exception e) {
						FlowUtils.getLogger().error("inject error!nodeBean=" + nodeBean, e);
					}
				}
				if (ann instanceof FlowParam) {
					try {
						Object value = FlowUtils.resolveParameter((FlowParam) ann, field.getType(), configuration);
						if (value != null) {
							if (!field.isAccessible()) {
								field.setAccessible(true);
							}
							field.set(nodeBean, value);
						}
					} catch (Exception e) {
						FlowUtils.getLogger().error("inject error!nodeBean=" + nodeBean, e);
					}
				}

			}

		}
	}

}
