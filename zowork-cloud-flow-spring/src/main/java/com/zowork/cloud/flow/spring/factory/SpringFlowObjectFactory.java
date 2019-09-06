package com.zowork.cloud.flow.spring.factory;

import com.zowork.cloud.flow.FlowConfiguration;
import com.zowork.cloud.flow.factory.DefaultFlowObjectFactory;
import com.zowork.cloud.flow.node.FlowActionTagNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringFlowObjectFactory extends DefaultFlowObjectFactory
        implements ApplicationContextAware, InitializingBean {
    ApplicationContext applicationContext;
    FlowConfiguration configuration;

    @Override
    public Object getObject(FlowActionTagNode node) {
        Object bean = null;
        if (node.getBeanClass() != null) {
            try {
                bean = applicationContext.getBean(node.getBeanClass());
            } catch (Throwable e) {
                logger.warn("get bean from spring error!", e);
            }
        }
        if (bean != null) {
            return bean;
        }
        if (StringUtils.isNotBlank(node.getBeanId())) {
            bean = applicationContext.getBean(node.getBeanId());

        }
        if (bean != null) {
            return bean;
        }

        if (StringUtils.isNotBlank(node.getRef())) {
            if (applicationContext.containsBean(node.getRef())) {
                return applicationContext.getBean(node.getRef());
            }
        }

        if (StringUtils.isNotBlank(node.getId())) {
            if (applicationContext.containsBean(node.getId())) {
                return applicationContext.getBean(node.getId());
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
