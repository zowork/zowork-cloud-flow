package com.zowork.cloud.flow.engine;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zowork.cloud.flow.FlowConfiguration;
import com.zowork.cloud.flow.FlowContext;
import com.zowork.cloud.flow.FlowException;
import com.zowork.cloud.flow.FlowResultBuilder;
import com.zowork.cloud.flow.FlowUtils;
import com.zowork.cloud.flow.invocation.FlowInvocation;
import com.zowork.cloud.flow.node.FlowTagNode;
import com.zowork.cloud.flow.trace.FlowTrace;

public class DefaultFlowEngine<T extends Serializable> implements FlowEngine<T> {
    static Logger logger = LoggerFactory.getLogger(DefaultFlowEngine.class);

    final FlowTagNode flow;
    final FlowConfiguration configuration;

    public DefaultFlowEngine(FlowTagNode flow, FlowConfiguration configuration) {
        super();
        this.flow = flow;
        this.configuration = configuration;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T process(FlowContext context) {
        try {
            /**
             * 创建流程执行器
             */
            final FlowInvocation invocation = new FlowInvocation(flow, configuration);
            context.setInvocation(invocation);
            context.getContextMap().put("$flow", invocation);// 将invocation放到上下文中
            Object value = invocation.invoke();
            if (value != null) {
                if (FlowUtils.isReturnResult(context, value)) {
                    return (T) value;
                }
            }
            return null;
            //return FlowResultBuilder.buildResult(context.getResultClass(), invocation.getConfiguration());
        } catch (Exception e) {
            logger.error("process error!", e);
            if (e instanceof InvocationTargetException) {
                Throwable target = ((InvocationTargetException) e).getTargetException();
                if (target instanceof RuntimeException) {
                    throw (RuntimeException) target;
                }
            }
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            if (e instanceof FlowException) {
                Throwable cause = e.getCause();
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException) cause;
                }
                if (cause instanceof InvocationTargetException) {
                    Throwable target = ((InvocationTargetException) cause).getTargetException();
                    if (target instanceof RuntimeException) {
                        throw (RuntimeException) target;
                    }
                }
                throw (FlowException) e;
            }
            throw new FlowException("5000", e.getMessage());
        }
    }

    @Override
    public FlowTrace trace(FlowContext context) {
        // TODO Auto-generated method stub
        return null;
    }

}
