package com.zowork.cloud.flow.spring;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import com.zowork.cloud.flow.FlowConfiguration;
import com.zowork.cloud.flow.annotation.FlowService;
import com.zowork.cloud.flow.proxy.FlowServiceProxy;

public class FlowServiceClassPathScanner extends ClassPathBeanDefinitionScanner {

	private Class<? extends Annotation> annotationClass = FlowService.class;

	private Class<?> baseInterface;
	final FlowConfiguration configuration;

	public FlowServiceClassPathScanner(BeanDefinitionRegistry registry, FlowConfiguration configuration) {
		super(registry, false);
		this.configuration = configuration;
	}

	/**
	 * Calls the parent search that will search and register all the candidates.
	 * Then the registered objects are post processed to set them as
	 * FlowServiceFactoryBeans
	 */
	@Override
	public Set<BeanDefinitionHolder> doScan(String... basePackages) {
		Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

		if (beanDefinitions.isEmpty()) {
			logger.warn("No FlowService was found in '" + Arrays.toString(basePackages)
					+ "' package. Please check your configuration.");
		} else {
			processBeanDefinitions(beanDefinitions);
		}

		return beanDefinitions;
	}

	public Class<? extends Annotation> getAnnotationClass() {
		return annotationClass;
	}

	public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
		this.annotationClass = annotationClass;
	}

	public Class<?> getBaseInterface() {
		return baseInterface;
	}

	public void setBaseInterface(Class<?> baseInterface) {
		this.baseInterface = baseInterface;
	}

	/**
	 * Configures parent scanner to search for the right interfaces. It can
	 * search for all interfaces or just for those that extends a baseInterface
	 * or/and those annotated with the annotationClass
	 */
	public void registerFilters() {
		boolean acceptAllInterfaces = true;

		// if specified, use the given annotation and / or marker interface
		if (this.annotationClass != null) {
			addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
			acceptAllInterfaces = false;
		}

		// override AssignableTypeFilter to ignore matches on the actual marker
		// interface
		if (this.baseInterface != null) {
			addIncludeFilter(new AssignableTypeFilter(this.baseInterface) {
				@Override
				protected boolean matchClassName(String className) {
					return false;
				}
			});
			acceptAllInterfaces = false;
		}

		if (acceptAllInterfaces) {
			// default include filter that accepts all classes
			addIncludeFilter(new TypeFilter() {
				@Override
				public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
						throws IOException {
					return true;
				}
			});
		}

		// exclude package-info.java
		addExcludeFilter(new TypeFilter() {
			@Override
			public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
					throws IOException {
				String className = metadataReader.getClassMetadata().getClassName();
				return className.endsWith("package-info");
			}
		});
	}

	private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
		GenericBeanDefinition definition;
		for (BeanDefinitionHolder holder : beanDefinitions) {
			definition = (GenericBeanDefinition) holder.getBeanDefinition();

			if (logger.isDebugEnabled()) {
				logger.debug("Creating FlowServiceFactoryBean with name '" + holder.getBeanName() + "' and '"
						+ definition.getBeanClassName() + "' serviceInterface");
			}
			String beanClassName = definition.getBeanClassName();
			Class<?> serviceInterface = null;
			try {
				serviceInterface = definition.getClass().getClassLoader().loadClass(beanClassName);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			definition.setBeanClass(FlowServiceFactoryBean.class);
			definition.getPropertyValues().addPropertyValue("serviceInterface", serviceInterface);
			FlowServiceProxy<?> proxy = new FlowServiceProxy<>(serviceInterface, configuration);
			definition.getPropertyValues().addPropertyValue("proxy", proxy);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) {
		if (super.checkCandidate(beanName, beanDefinition)) {
			return true;
		} else {
			logger.warn("Skipping FlowServiceFactoryBean with name '" + beanName + "' and '"
					+ beanDefinition.getBeanClassName() + "' serviceInterface"
					+ ". Bean already defined with the same name!");
			return false;
		}
	}

}
