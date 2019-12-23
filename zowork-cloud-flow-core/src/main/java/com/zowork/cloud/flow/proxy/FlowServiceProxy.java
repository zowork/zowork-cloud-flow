package com.zowork.cloud.flow.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Stack;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;

import com.zowork.cloud.flow.FlowConfiguration;
import com.zowork.cloud.flow.FlowContext;
import com.zowork.cloud.flow.FlowUtils;
import com.zowork.cloud.flow.annotation.FlowService;
import com.zowork.cloud.flow.annotation.FlowServiceMethod;
import com.zowork.cloud.flow.engine.FlowEngine;

public class FlowServiceProxy<T> implements InvocationHandler {
    @Resource
    FlowConfiguration configration;
    private final Class<T> serviceInterface;

    public FlowServiceProxy(Class<T> serviceInterface, FlowConfiguration configration) {
        super();
        this.serviceInterface = serviceInterface;
        this.configration = configration;
    }

    /**
     * 代理类
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        FlowServiceMethod flowMethod = method.getAnnotation(FlowServiceMethod.class);
        FlowService flowService = AnnotationUtils.findAnnotation(proxy.getClass(), FlowService.class);
        String flowId = method.getName();

        if (flowMethod != null && StringUtils.isNotBlank(flowMethod.id())) {
            flowId = flowMethod.id();
        }
        String namespace = serviceInterface.getName();
        if (flowService != null && StringUtils.isNotBlank(flowService.namespace())) {
            namespace = flowService.namespace();
        }
        FlowContext context = this.createContext(method, args);
        context.setNamespace(namespace);
        context.setFlowId(flowId);
        Object model = null;
        try {
            context.pushStack(context);
            FlowEngine<?> flowEngine = configration.getFlowEngineManager().getEngine(namespace, flowId);
            model = flowEngine.process(context);
        } finally {
            context = context.popStack();

            if (context.getInvokeStack().size() == 0) {//当调用栈已经变成0，则可以清掉context了
                FlowContext.cleanup();
            } else {
                FlowContext.setContext(context.getInvokeStack().peek());//设置父流程的context,不要从stack中删除
            }
        }
        return model;
    }

    FlowContext createContext(Method method, Object[] args) {

        FlowContext context = FlowContext.getContext();
        if (context == null) {//没有父流程的处理
            context = new FlowContext();
            Stack<FlowContext> invokeStack = new Stack<FlowContext>();//第一次初始化context设置stack
            context.setInvokeStack(invokeStack);
        } else {//有父流程的处理将父流程的invokeStack设置给子Context
            Stack<FlowContext> invokeStack = context.getInvokeStack();
            context = new FlowContext();
            context.setInvokeStack(invokeStack);
        }

        Map<String, Object> contextMap = FlowUtils.resolveContextMap(method, args);
        context.setContextMap(contextMap);
        context.setServiceInterface(serviceInterface);
        context.setResultClass(method.getReturnType());
        context.setMethodArgs(args);
        context.setRequest(FlowUtils.getRequest());
        context.setResponse(FlowUtils.getResponse());
        context.setSession(FlowUtils.getSession());
        return context;
    }

}
