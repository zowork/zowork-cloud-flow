package com.zowork.cloud.flow.config;

import com.zowork.cloud.flow.FlowConfiguration;
import com.zowork.cloud.flow.node.FlowActionTagNode;
import com.zowork.cloud.flow.node.FlowElement;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;

public class DefaultParseContext implements ParseContext {
    final FlowConfiguration configuration;
    String namespace;
    String flowId;

    public DefaultParseContext(FlowConfiguration configuration, String namespace) {
        super();
        this.configuration = configuration;
        this.namespace = namespace;
    }

    @Override
    public FlowConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    @Override
    public String getFlowId() {
        return flowId;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    @Override
    public void registry(String id, FlowElement node) {
        configuration.registry(namespace, flowId, id, node);
//        if (node instanceof FlowActionTagNode) {
//            if (parserContext != null) {
//                FlowActionTagNode actionTagNode = (FlowActionTagNode) node;
//                Class<?> beanClass = actionTagNode.getBeanClass();
//                BeanDefinition beanDefinition = new RootBeanDefinition(beanClass);
//
//                String beanName = beanNameGenerator.generateBeanName(beanDefinition, parserContext.getRegistry());
//
//                if (!parserContext.getRegistry().containsBeanDefinition(beanName)) {
//                    parserContext.getRegistry().registerBeanDefinition(beanName, beanDefinition);
//                }
//            }
//        }
    }

}
