package com.zowork.cloud.flow.spring;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;

import com.zowork.cloud.flow.proxy.FlowServiceProxy;

/**
 * 实现service的FactoryBean
 * 
 * @author luolishu
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class FlowServiceFactoryBean implements FactoryBean {
	Class serviceInterface;
	FlowServiceProxy proxy;
	Object bean;

	@Override
	public Object getObject() throws Exception {
		if (bean == null) {
			bean = getService(serviceInterface);
		}
		return bean;
	}

	<T> T getService(Class<T> claz) {
		T bean = (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { serviceInterface }, proxy);
		return bean;

	}

	@Override
	public Class getObjectType() {
		return serviceInterface;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public Class getServiceInterface() {
		return serviceInterface;
	}

	public void setServiceInterface(Class serviceInterface) {
		this.serviceInterface = serviceInterface;
	}

	public FlowServiceProxy getProxy() {
		return proxy;
	}

	public void setProxy(FlowServiceProxy proxy) {
		this.proxy = proxy;
	}

}
