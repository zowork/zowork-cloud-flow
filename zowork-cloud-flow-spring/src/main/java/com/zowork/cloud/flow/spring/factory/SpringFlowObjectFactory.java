package com.zowork.cloud.flow.spring.factory;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.zowork.cloud.flow.FlowConfiguration;
import com.zowork.cloud.flow.factory.DefaultFlowObjectFactory;
import com.zowork.cloud.flow.node.FlowActionTagNode;

public class SpringFlowObjectFactory extends DefaultFlowObjectFactory
		implements ApplicationContextAware, InitializingBean {
	ApplicationContext applicationContext;
	FlowConfiguration configuration;

	@Override
	public Object getObject(FlowActionTagNode node) {
		Object bean = null;
		if (StringUtils.isNotBlank(node.getRef())) {
			bean = applicationContext.getBean(node.getRef());
		}
		if (bean == null && node.getBeanClass() != null) {
			try {
				bean = applicationContext.getBean(node.getBeanClass());
			} catch (Exception e) {
				logger.warn("get bean from spring error!", e);
			}
		}
		return super.getObject(node);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public FlowConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(FlowConfiguration configuration) {
		this.configuration = configuration;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		configuration.setObjectFactory(this);
	}

}
