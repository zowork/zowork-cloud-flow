package com.zowork.cloud.flow.resolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.SynthesizingMethodParameter;

import com.zowork.cloud.flow.FlowConfiguration;
import com.zowork.cloud.flow.FlowContext;
import com.zowork.cloud.flow.FlowUtils;
import com.zowork.cloud.flow.annotation.FlowAttribute;
import com.zowork.cloud.flow.annotation.FlowParam;
import com.zowork.cloud.flow.converter.FlowTypeConverter;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;

public class DefaultArgumentsResolver implements ArgumentsResolver {
    FlowConfiguration configuration;
    static DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    static Set<Class<?>> classSet = new HashSet<Class<?>>();

    static {
        classSet.add(FlowContext.class);
    }

    public DefaultArgumentsResolver(FlowConfiguration configuration) {
        super();
        this.configuration = configuration;
    }

    public FlowConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(FlowConfiguration configuration) {
        this.configuration = configuration;
    }

    public Object[] resolveMethodArguments(Method handlerMethod, Object action) throws Exception {

        Class<?>[] paramTypes = handlerMethod.getParameterTypes();
        Object[] args = new Object[paramTypes.length];
        for (int i = 0; i < args.length; i++) {
            Class<?> parameterType = paramTypes[i];
            if (FlowContext.class.isAssignableFrom(parameterType)) {
                args[i] = FlowContext.getContext();
                continue;
            }
            MethodParameter methodParam = new SynthesizingMethodParameter(handlerMethod, i);
            methodParam.initParameterNameDiscovery(parameterNameDiscoverer);
            GenericTypeResolver.resolveParameterType(methodParam, action.getClass());
            Annotation[] paramAnns = methodParam.getParameterAnnotations();
            if (ArrayUtils.isEmpty(paramAnns)) {
                if (ClassUtils.isPrimitiveOrWrapper(parameterType)) {
                    continue;
                } else {
                    if (Map.class.isAssignableFrom(parameterType)) {//假如是Map把Param和Attribute都放里面
                        Map<String, Object> argmentsMap = new LinkedHashMap<>();
                        args[i] = argmentsMap;
                        argmentsMap.putAll(FlowContext.getContext().getParametersMap());
                        argmentsMap.putAll(FlowContext.getContext().getAttributesMap());
                    } else {
                        if (!StringUtils.startsWithAny(parameterType.getName(), "java.util.", "javax.", "java.", "org.")) {
                            Object argValue = ConstructorUtils.invokeConstructor(parameterType);
                            args[i] = argValue;
                            Field[] fields = FieldUtils.getAllFields(parameterType);
                            FlowContext context = FlowContext.getContext();
                            for (Field field : fields) {
                                Object value = resolveFieldValue(field, context);
                                if (value == null) {
                                    continue;
                                }
                                FieldUtils.writeField(field, argValue, configuration.getTypeConverter().convertIfNecessary(value, field.getType()), true);
                            }
                        }
                    }
                }

            } else {
                for (Annotation paramAnn : paramAnns) {
                    if (FlowParam.class.isInstance(paramAnn)) {
                        args[i] = resolveFlowParam((FlowParam) paramAnn, methodParam);
                    }
                    if (FlowAttribute.class.isInstance(paramAnn)) {
                        args[i] = resolveFlowAttribute((FlowAttribute) paramAnn, methodParam);
                    }
                }
            }
            if (args[i] == null) {//假如是空，根据传参进行匹配对象类型
                if (HttpServletRequest.class.isAssignableFrom(parameterType)) {
                    args[i] = FlowContext.getContext().getRequest();
                    continue;
                }
                Object[] methodArgs = FlowContext.getContext().getMethodArgs();
                if (methodArgs == null) {
                    continue;
                }
                for (Object methodArg : methodArgs) {
                    if (methodArg == null) {
                        continue;
                    }
                    if (parameterType.isAssignableFrom(methodArg.getClass())
                            && classSet.contains(methodArg.getClass())) {
                        args[i] = methodArg;
                        break;
                    }
                }
            }
        }
        return args;
    }

    private Object resolveFieldValue(Field field, FlowContext context) throws Exception {

        Object value = null;
        FlowParam flowParam = field.getAnnotation(FlowParam.class);
        if (flowParam != null) {
            value = FlowUtils.resolveParameter(flowParam, field.getType(), configuration);
        }
        if (value != null) {
            return value;
        }
        FlowAttribute flowAttribute = field.getAnnotation(FlowAttribute.class);
        if (flowAttribute != null) {
            value = FlowUtils.resolveAttribute(flowAttribute, field.getType(), configuration);
        }
        if (value != null) {
            return value;
        }
        value = context.getParametersMap().get(field.getName());
        if (value != null) {
            return value;
        }
        value = context.getAttributesMap().get(field.getName());
        return value;

    }

    private Object resolveFlowParam(FlowParam paramAnn, MethodParameter methodParam) throws Exception {
        return FlowUtils.resolveParameter(paramAnn, methodParam, configuration);

    }

    private Object resolveFlowAttribute(FlowAttribute attrAnn, MethodParameter methodParam) throws Exception {
        return FlowUtils.resolveAttribute(attrAnn, methodParam, configuration);

    }

    FlowTypeConverter getTypeConverter() {
        return configuration.getTypeConverter();
    }

    public static void main(String args[]) {
        System.out.println(Map.class.isAssignableFrom(HashMap.class));
        System.out.println(HashMap.class.isAssignableFrom(Map.class));
    }
}
