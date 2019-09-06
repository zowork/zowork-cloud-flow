/**
 * 
 */
package com.zowork.cloud.flow.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.zowork.cloud.flow.FlowConfiguration;

/**
 * @author luolishu
 *
 */
public class FlowNamespaceHandler extends NamespaceHandlerSupport {

	public static final FlowConfiguration configuration = new FlowConfiguration();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.xml.NamespaceHandler#init()
	 */
	@Override
	public void init() {
		registerBeanDefinitionParser("import", new FlowBeanDefinitionParser(configuration));
		registerBeanDefinitionParser("component-scan", new ComponentScanFlowDefinitionParser(configuration));

	}

}
