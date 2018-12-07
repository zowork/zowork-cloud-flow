package com.zowork.cloud.flow.resolver;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.MethodUtils;

import com.zowork.cloud.flow.FlowException;
import com.zowork.cloud.flow.annotation.FlowAction;
import com.zowork.cloud.flow.annotation.FlowValidate;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.support.AopUtils;

public class DefaultMethodResolver implements MethodResolver {

    @Override
    public Method resolveValidate(Object nodeBean) {
        String methodNames[] = {"validate", "doValidate"};
        Method[] methodArray = MethodUtils.getMethodsWithAnnotation(nodeBean.getClass(), FlowValidate.class, true, true);
        if (ArrayUtils.isEmpty(methodArray)) {
            methodArray = getMatchingMethod(nodeBean.getClass(), methodNames);
        }
        if (ArrayUtils.isEmpty(methodArray)) {
            return null;
        }
        if (methodArray.length > 1) {
            throw new FlowException("5000", "duplicate validate methods!");
        }
        return methodArray[0];
    }

    public static Method[] getMatchingMethod(final Class<?> cls, final String[] methodNames) {
        Validate.notNull(cls, "Null class not allowed.");
        Validate.notEmpty(methodNames, "Null or blank methodName not allowed.");

        // Address methods in superclasses
        Method[] methodArray = cls.getDeclaredMethods();
        final List<Class<?>> superclassList = ClassUtils.getAllSuperclasses(cls);
        for (final Class<?> klass : superclassList) {
            methodArray = ArrayUtils.addAll(methodArray, klass.getDeclaredMethods());
        }

        List<Method> methodList = new LinkedList<>();
        for (final Method method : methodArray) {
            if (ArrayUtils.contains(methodNames, method.getName())) {
                methodList.add(method);
            }
        }
        return methodList.toArray(new Method[]{});
    }
    public Object getTarget(Object beanInstance) {
        if (!AopUtils.isAopProxy(beanInstance)) {
            return beanInstance;
        } else if (AopUtils.isCglibProxy(beanInstance)) {
            try {
                Field field = beanInstance.getClass().getDeclaredField("CGLIB$CALLBACK_0");
                field.setAccessible(true);
                Object dynamicAdvisedInterceptor = field.get(beanInstance);

                Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
                advised.setAccessible(true);

                Object target = ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
                return target;
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;

    }
    @Override
    public Method resolveAction(Object nodeBean) {
        nodeBean=getTarget(nodeBean);
        String methodNames[] = {"execute", "doExecute"};
        Method[] methodArray = MethodUtils.getMethodsWithAnnotation(nodeBean.getClass(), FlowAction.class, true, true);
        if (ArrayUtils.isEmpty(methodArray)) {
            methodArray = getMatchingMethod(nodeBean.getClass(), methodNames);
        }
        if (ArrayUtils.isEmpty(methodArray)) {
            throw new FlowException("5000", "Action method not exist!nodeBean=" + nodeBean.getClass());
        }
        if (methodArray.length > 1) {
            throw new FlowException("5000", "duplicate Action methods!nodeBean=" + nodeBean.getClass());
        }
        return methodArray[0];
    }

}
