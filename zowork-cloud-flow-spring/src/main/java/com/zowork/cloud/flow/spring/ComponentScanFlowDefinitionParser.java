package com.zowork.cloud.flow.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import com.zowork.cloud.flow.FlowConfiguration;

public class ComponentScanFlowDefinitionParser implements BeanDefinitionParser {
	private static final String BASE_PACKAGE_ATTRIBUTE = "base-package";
	FlowConfiguration configuration;

	public ComponentScanFlowDefinitionParser(FlowConfiguration configuration) {
		super();
		this.configuration = configuration;
	}

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		String basePackage = element.getAttribute(BASE_PACKAGE_ATTRIBUTE);
		basePackage = parserContext.getReaderContext().getEnvironment().resolvePlaceholders(basePackage);
		String[] basePackages = StringUtils.tokenizeToStringArray(basePackage,
				ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);

		// Actually scan for bean definitions and register them.
		FlowServiceClassPathScanner scanner = new FlowServiceClassPathScanner(parserContext.getRegistry(),
				configuration);
		scanner.registerFilters();
		scanner.scan(basePackages);

		return null;
	}

}
