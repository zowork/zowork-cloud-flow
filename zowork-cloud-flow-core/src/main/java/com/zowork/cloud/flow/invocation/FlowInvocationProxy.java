package com.zowork.cloud.flow.invocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zowork.cloud.flow.FlowConfiguration;
import com.zowork.cloud.flow.FlowContext;
import com.zowork.cloud.flow.FlowException;
import com.zowork.cloud.flow.node.FlowActionTagNode;
import com.zowork.cloud.flow.node.FlowElement;
import com.zowork.cloud.flow.node.FlowScriptTagNode;

public class FlowInvocationProxy {
    static Logger logger = LoggerFactory.getLogger(FlowInvocationProxy.class);
    final FlowElement node;
    final FlowInvocation invocation;
    final FlowContext context;
    final FlowConfiguration configuration;

    public FlowInvocationProxy(FlowElement node, FlowInvocation invocation, FlowContext context) {
        super();
        this.node = node;
        this.invocation = invocation;
        this.context = context;
        this.configuration = invocation.getConfiguration();
    }

    public Object invokeActionOnly() {
        if (node instanceof FlowActionTagNode) {
            try {
                return this.invokeActionNode();
            } catch (Exception e) {
                logger.error("invokeActionNode error!node=" + node, e);
                if (e instanceof InvocationTargetException) {
                    InvocationTargetException targetException = (InvocationTargetException) e;
                    if (targetException.getTargetException() instanceof RuntimeException) {
                        throw (RuntimeException) targetException.getTargetException();
                    }
                }
                if (e.getCause() != null) {
                    throw new FlowException(e.getCause());
                } else {
                    throw new FlowException(e);
                }

            }
        }
        if (node instanceof FlowScriptTagNode) {
            try {
                return this.invokeScriptNode();
            } catch (Exception e) {
                logger.error("invokeScriptNode error!", e);
                if (e.getCause() != null) {
                    throw new FlowException(e.getCause());
                } else {
                    throw new FlowException(e);
                }
            }
        }
        return null;
    }

    public FlowElement getNode() {
        return node;
    }

    public FlowInvocation getInvocation() {
        return invocation;
    }

    Object invokeActionNode() throws Exception {
        FlowActionTagNode actionNode = (FlowActionTagNode) node;
        configuration.getAutowiredResolver().resolve(actionNode.getBean());
        Object validateResult = this.invokeValidateIfNecessary(actionNode.getBean());
        if (!check(validateResult)) {
            return validateResult;
        }
        Object result = this.invokeActionIfNecessary(actionNode.getBean());
        return result;
    }

    Object invokeScriptNode() throws Exception {
        FlowScriptTagNode scriptNode = (FlowScriptTagNode) node;
        Object result = configuration.getScriptExecutor().execute(scriptNode.getScript(), this.context);
        return result;
    }

    /**
     * 检查校验结果
     *
     * @param validateResult
     * @return
     */
    boolean check(Object validateResult) {
        if (validateResult instanceof Boolean) {
            return ((Boolean) validateResult).booleanValue();
        }
        return validateResult == null;// 假如返回非空对象
    }

    Object invokeValidateIfNecessary(Object nodeBean) throws Exception {
        Method method = this.getValidateMethod();
        if (method == null) {
            return true;
        }
        Object[] args = configuration.getArgumentsResolver().resolveMethodArguments(method, nodeBean);
        return method.invoke(nodeBean, args);
    }

    Object invokeActionIfNecessary(Object nodeBean) throws Exception {
        Method method = this.getActionMethod();
        Object[] args = configuration.getArgumentsResolver().resolveMethodArguments(method, nodeBean);
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        try {
            Object result = method.invoke(nodeBean, args);
            result = configuration.getFlowResultHandler().handle(method, result);
            return result;
        } catch (Throwable e) {
            logger.error("invoke error!action=" + nodeBean + ",method=" + method, e);
            throw e;
        }
    }

    Method getValidateMethod() {
        FlowActionTagNode actionNode = (FlowActionTagNode) node;
        return actionNode.getValidateMethod();
    }

    Method getActionMethod() {
        FlowActionTagNode actionNode = (FlowActionTagNode) node;
        return actionNode.getActionMethod();
    }
}
