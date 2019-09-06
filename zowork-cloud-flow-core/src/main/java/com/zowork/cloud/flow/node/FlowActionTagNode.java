package com.zowork.cloud.flow.node;

import com.zowork.cloud.flow.FlowConfiguration;
import com.zowork.cloud.flow.FlowUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

/**
 * 执行节点
 */
public class FlowActionTagNode extends BaseTagNode implements FlowElement, Executable {
    private static final long serialVersionUID = -5905524112296008557L;
    String className;
    boolean async;
    Class<?> beanClass;
    String ref;
    Object bean;
    String flowId;
    String beanId;

    public FlowActionTagNode(FlowConfiguration configuration, String id) {
        super();
        this.configuration = configuration;
        this.id = id;
        if (StringUtils.isNotBlank(id)) {
            this.beanId = flowId + "_" + id;
        }
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
        if (StringUtils.isNotBlank(className) && beanClass == null) {
            try {
                beanClass = ClassUtils.getClass(className);
            } catch (ClassNotFoundException e) {
                FlowUtils.getLogger().error("flow class error!className=" + className + ",action=" + this);
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public Object getBean() {
        if (bean == null) {
            bean = configuration.getObjectFactory().getObject(this);
        }
        return bean;
    }

    public String getBeanId() {
        return beanId;
    }

    public void setBeanId(String beanId) {
        this.beanId = beanId;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Method getValidateMethod() {
        return configuration.getMethodResover().resolveValidate(bean);
    }

    public Method getActionMethod() {
        return configuration.getMethodResover().resolveAction(bean);
    }

}
