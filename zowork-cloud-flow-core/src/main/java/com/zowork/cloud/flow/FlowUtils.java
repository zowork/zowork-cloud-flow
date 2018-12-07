package com.zowork.cloud.flow;

import com.zowork.cloud.flow.annotation.FlowAttribute;
import com.zowork.cloud.flow.annotation.FlowParam;
import com.zowork.cloud.flow.node.*;
import com.zowork.cloud.flow.web.RequestResolver;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

@Component
public class FlowUtils {
    public static String DEFAULT_NAME_SPACE = "_default_flow_space";
    static final Logger LOGGER = LoggerFactory.getLogger(FlowUtils.class);
    static RequestResolver requestResolver;

    /**
     * 是否返回结果
     *
     * @return
     */
    public static boolean isReturnResult(FlowContext context, Object result) {
        if (result == null) {
            return false;
        }
        if (context.getResultClass().isAssignableFrom(result.getClass())) {
            return true;
        }
        return false;
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public static boolean isExecutable(FlowElement el) {
        return el instanceof Executable;
    }

    public static boolean isFlowNode(FlowElement el) {
        return el instanceof FlowTagNode;
    }

    public static boolean isRootNode(FlowElement el) {
        return el instanceof FlowsRootTagNode;
    }

    public static boolean isForwardNode(FlowElement el) {
        return el instanceof FlowForwardTagNode;
    }

    public static boolean isScriptNode(FlowElement el) {
        return el instanceof FlowScriptTagNode;
    }

    public static boolean isActionNode(FlowElement el) {
        return el instanceof FlowScriptTagNode;
    }

    public static boolean isChooseNode(FlowElement el) {
        return el instanceof FlowChooseTagNode;
    }

    public static boolean isIfNode(FlowElement el) {
        return el instanceof FlowIfTagNode && !(el.parent() instanceof FlowChooseTagNode);
    }

    public static boolean isElseIfNode(FlowElement el) {
        return el instanceof FlowElseIfTagNode && !(el.parent() instanceof FlowChooseTagNode);
    }

    public static boolean isElseNode(FlowElement el) {
        return el instanceof FlowElseTagNode && !(el.parent() instanceof FlowChooseTagNode);
    }

    public static boolean isChooseSubNode(FlowElement el) {
        return el instanceof FlowIfTagNode && el.parent() instanceof FlowChooseTagNode;
    }

    public static boolean isOtherwiseNode(FlowElement el) {
        return isChooseSubNode(el) && StringUtils.isBlank(((FlowIfTagNode) el).getTest());
    }

    public static boolean isWhenNode(FlowElement el) {
        return isChooseSubNode(el) && StringUtils.isNotBlank(((FlowIfTagNode) el).getTest());
    }

    public static boolean isSubFlowNode(FlowElement el) {
        return el instanceof FlowSubFlowTagNode;
    }

    public static Map<String, Object> toMap(Object bean) {
        return null;
    }

    public static String getFlowName(String namespace, String flowId) {
        if (StringUtils.isBlank(namespace)) {
            namespace = DEFAULT_NAME_SPACE;
        }
        String flowName = namespace + flowId;
        return flowName;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Map<String, Object> resolveContextMap(Method method, Object[] args) {
        Annotation[][] annArray = method.getParameterAnnotations();
        FlowContext context = FlowContext.getContext();
        FlowContextMap contextMap = new FlowContextMap(FlowContext.getContext());
        if (args == null || args.length == 0) {
            return contextMap;
        }

        for (int i = 0; i < args.length; i++) {
            Annotation[] anns = annArray[i];
            Object value = args[i];
            if (value == null) {
                continue;
            }
            if (ArrayUtils.isEmpty(anns)) {
                if (value instanceof Map) {
                    context.getParametersMap().putAll((Map) value);
                    continue;
                }
                if (value.getClass().getName().startsWith("java.")) {
                    continue;
                }
                Field[] fieldArr = FieldUtils.getAllFields(value.getClass());
                for (Field field : fieldArr) {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    try {
                        context.setParameter(field.getName(), field.get(value));
                    } catch (Exception e) {
                        LOGGER.error("set parameter error!", e);
                    }
                }
                contextMap.addArgument(value);

                continue;
            }
            for (Annotation ann : anns) {
                if (ann instanceof FlowParam) {
                    FlowParam flowParam = (FlowParam) ann;

                    for (String key : flowParam.value()) {
                        contextMap.put(key, value);
                        context.setParameter(key, value);
                    }

                } else {
                    contextMap.addArgument(value);
                }
            }
        }
        return contextMap;
    }

    public static Object resolveParameter(FlowParam paramAnn, MethodParameter methodParam,
                                          FlowConfiguration configuration) throws Exception {

        Class<?> paramType = methodParam.getParameterType();
        return resolveParameter(paramAnn, paramType, configuration);

    }

    public static Object resolveParameter(FlowParam paramAnn, Class<?> paramType, FlowConfiguration configuration)
            throws Exception {

        Object value = null;
        FlowContext context = FlowContext.getContext();
        for (String expression : paramAnn.value()) {
            Object param = context.getParameter(expression);
            if (param != null) {
                value = param;
            }
            if (value == null) {
                value = configuration.getExpressionValueResolver().resolveObject("$Param." + expression,
                        context.getContextMap());
            }
        }
        return configuration.getTypeConverter().convertIfNecessary(value, paramType);

    }

    public static Object resolveAttribute(FlowAttribute attrAnn, MethodParameter methodParam,
                                          FlowConfiguration configuration) throws Exception {

        Class<?> paramType = methodParam.getParameterType();
        return resolveAttribute(attrAnn, paramType, configuration);

    }

    public static Object resolveAttribute(FlowAttribute attrAnn, Class<?> paramType, FlowConfiguration configuration)
            throws Exception {

        Object value = null;
        FlowContext context = FlowContext.getContext();
        for (String expression : attrAnn.value()) {
            Object param = context.getAttribute(expression);
            if (param != null) {
                value = param;
            }
            if (value == null) {
                value = configuration.getExpressionValueResolver().resolveObject("$Attr." + expression,
                        context.getContextMap());
            }
        }

        return configuration.getTypeConverter().convertIfNecessary(value, paramType);

    }

    public static Map<String, Object> resolveServiceContextMap(Method method, Object params) {
        FlowContextMap contextMap = new FlowContextMap(FlowContext.getContext());
        if (params != null) {
            contextMap.addArgument(params);
        }
        return contextMap;
    }

    public static HttpServletRequest getRequest() {
        return requestResolver.getRequest();
    }

}
