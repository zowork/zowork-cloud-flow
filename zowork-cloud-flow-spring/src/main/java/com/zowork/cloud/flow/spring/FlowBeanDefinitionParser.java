package com.zowork.cloud.flow.spring;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.DefaultDocumentLoader;
import org.springframework.beans.factory.xml.DelegatingEntityResolver;
import org.springframework.beans.factory.xml.DocumentLoader;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.CollectionUtils;
import org.springframework.util.xml.XmlValidationModeDetector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.zowork.cloud.flow.FlowConfiguration;
import com.zowork.cloud.flow.config.DefaultParseContext;
import com.zowork.cloud.flow.config.FlowParser;
import com.zowork.cloud.flow.config.FlowParserFactory;
import com.zowork.cloud.flow.config.ParseContext;
import com.zowork.cloud.flow.node.FlowActionTagNode;
import com.zowork.cloud.flow.node.FlowElement;
import com.zowork.cloud.flow.node.FlowTagNode;
import com.zowork.cloud.flow.node.FlowsRootTagNode;
import com.zowork.cloud.flow.spring.factory.SpringFlowObjectFactory;

/**
 * flow 解析类
 * 
 * @author luolishu
 *
 */
public class FlowBeanDefinitionParser implements BeanDefinitionParser, ResourceLoaderAware {
	static Logger logger = LoggerFactory.getLogger(FlowBeanDefinitionParser.class);
	FlowConfiguration configuration;
	ResourceLoader resourceLoader;
	DocumentLoader documentLoader = new DefaultDocumentLoader();
	private EntityResolver entityResolver;
	/**
	 * Indicates that the validation should be disabled.
	 */
	public static final int VALIDATION_NONE = XmlValidationModeDetector.VALIDATION_NONE;

	/**
	 * Indicates that the validation mode should be detected automatically.
	 */
	public static final int VALIDATION_AUTO = XmlValidationModeDetector.VALIDATION_AUTO;

	/**
	 * Indicates that DTD validation should be used.
	 */
	public static final int VALIDATION_DTD = XmlValidationModeDetector.VALIDATION_DTD;

	/**
	 * Indicates that XSD validation should be used.
	 */
	public static final int VALIDATION_XSD = XmlValidationModeDetector.VALIDATION_XSD;
	private int validationMode = VALIDATION_AUTO;
	private final XmlValidationModeDetector validationModeDetector = new XmlValidationModeDetector();
	ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
	BeanNameGenerator beanNameGenerator = new DefaultBeanNameGenerator();

	public FlowBeanDefinitionParser(FlowConfiguration configuration) {
		super();
		this.configuration = configuration;
	}

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		String locationPattern = element.getAttribute("resource");

		try {
			Resource[] resourceList = resourceResolver.getResources(locationPattern);
			for (Resource resource : resourceList) {
				EncodedResource encodedResource = new EncodedResource(resource);
				InputSource inputSource = new InputSource(resource.getInputStream());
				if (encodedResource.getEncoding() != null) {
					inputSource.setEncoding(encodedResource.getEncoding());
				}
				Document document = documentLoader.loadDocument(inputSource, getEntityResolver(), new ErrorHandler() {

					@Override
					public void warning(SAXParseException ex) throws SAXException {

					}

					@Override
					public void error(SAXParseException ex) throws SAXException {
						logger.error("resource=======" + resource, ex);
						throw ex;
					}

					@Override
					public void fatalError(SAXParseException ex) throws SAXException {
						logger.error("resource=======" + resource, ex);
						throw ex;
					}

				}, getValidationModeForResource(resource), true);
				NodeList nodeList = document.getChildNodes();
				if (nodeList.getLength() <= 0) {
					continue;
				}
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node item = nodeList.item(i);
					if (!(item instanceof Element)) {
						continue;
					}
					Element node = (Element) item;
					FlowParser flowParser = FlowParserFactory.create(node);
					if (flowParser == null) {
						logger.error("flowParser is null!node=" + node);
						continue;
					}
					String namespace = node.getAttribute("namespace");
					ParseContext parseContext = new DefaultParseContext(configuration, namespace);
					FlowElement root = flowParser.parse(node, parseContext);
					assert root != null;
					FlowsRootTagNode rootNode = (FlowsRootTagNode) root;
					this.registryFlows(rootNode, parserContext);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BeanDefinition factoryBeanDefinition = new RootBeanDefinition(SpringFlowObjectFactory.class);
		factoryBeanDefinition.getPropertyValues().addPropertyValue("configuration", configuration);
		String beanName = beanNameGenerator.generateBeanName(factoryBeanDefinition, parserContext.getRegistry());
		if (!parserContext.getRegistry().containsBeanDefinition(beanName)) {
			parserContext.getRegistry().registerBeanDefinition(beanName, factoryBeanDefinition);
		}

		List<FlowElement> actionNodeList = configuration.getActionNodes();
		Set<String> beanClassSet = new LinkedHashSet<>();
		for (FlowElement node : actionNodeList) {
			FlowActionTagNode actionNode = (FlowActionTagNode) node;
			if (StringUtils.isBlank(actionNode.getClassName()) || beanClassSet.contains(actionNode.getClassName())) {
				continue;
			}
			beanClassSet.add(actionNode.getClassName());
			BeanDefinition beanDefinition = new RootBeanDefinition(actionNode.getBeanClass());
			beanName = beanNameGenerator.generateBeanName(beanDefinition, parserContext.getRegistry());
			if (parserContext.getRegistry().containsBeanDefinition(beanName)) {
				continue;
			}
			parserContext.getRegistry().registerBeanDefinition(beanName, beanDefinition);
		}
		return null;
	}

	protected void registryFlows(FlowsRootTagNode rootNode, ParserContext parserContext) {
		if (CollectionUtils.isEmpty(rootNode.childs())) {
			return;
		}
		for (FlowElement flow : rootNode.getChilds()) {
			this.registryFlow((FlowTagNode) flow, parserContext);
		}
	}

	protected void registryFlow(FlowTagNode flow, ParserContext parserContext) {
		if (StringUtils.isBlank(flow.getServiceClass())) {
			return;
		}
		BeanDefinition beanDefinition = new RootBeanDefinition(flow.getServiceClass());
		String beanName = beanNameGenerator.generateBeanName(beanDefinition, parserContext.getRegistry());
		if (parserContext.getRegistry().containsBeanDefinition(beanName)) {
			return;
		}
		parserContext.getRegistry().registerBeanDefinition(beanName, beanDefinition);
	}

	protected int getValidationModeForResource(Resource resource) {
		int validationModeToUse = getValidationMode();
		if (validationModeToUse != VALIDATION_AUTO) {
			return validationModeToUse;
		}
		int detectedMode = detectValidationMode(resource);
		if (detectedMode != VALIDATION_AUTO) {
			return detectedMode;
		}
		// Hmm, we didn't get a clear indication... Let's assume XSD,
		// since apparently no DTD declaration has been found up until
		// detection stopped (before finding the document's root tag).
		return VALIDATION_XSD;
	}

	public int getValidationMode() {
		return this.validationMode;
	}

	/**
	 * Detects which kind of validation to perform on the XML file identified by
	 * the supplied {@link Resource}. If the file has a {@code DOCTYPE}
	 * definition then DTD validation is used otherwise XSD validation is
	 * assumed.
	 * <p>
	 * Override this method if you would like to customize resolution of the
	 * {@link #VALIDATION_AUTO} mode.
	 */
	protected int detectValidationMode(Resource resource) {
		if (resource.isOpen()) {
			throw new BeanDefinitionStoreException("Passed-in Resource [" + resource + "] contains an open stream: "
					+ "cannot determine validation mode automatically. Either pass in a Resource "
					+ "that is able to create fresh streams, or explicitly specify the validationMode "
					+ "on your XmlBeanDefinitionReader instance.");
		}

		InputStream inputStream;
		try {
			inputStream = resource.getInputStream();
		} catch (IOException ex) {
			throw new BeanDefinitionStoreException(
					"Unable to determine validation mode for [" + resource + "]: cannot open InputStream. "
							+ "Did you attempt to load directly from a SAX InputSource without specifying the "
							+ "validationMode on your XmlBeanDefinitionReader instance?",
					ex);
		}

		try {
			return this.validationModeDetector.detectValidationMode(inputStream);
		} catch (IOException ex) {
			throw new BeanDefinitionStoreException("Unable to determine validation mode for [" + resource
					+ "]: an error occurred whilst reading from the InputStream.", ex);
		}
	}

	protected EntityResolver getEntityResolver() {
		if (this.entityResolver == null) {
			if (resourceLoader != null) {
				this.entityResolver = new ResourceEntityResolver(resourceLoader);
			} else {
				this.entityResolver = new DelegatingEntityResolver(FlowBeanDefinitionParser.class.getClassLoader());
			}
		}
		return this.entityResolver;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

}
