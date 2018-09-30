package com.zowork.cloud.flow;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;

public class FlowContextMap extends HashMap<String, Object> {
	private static final long serialVersionUID = -7157731958868798527L;
	FlowContext context;
	List<MetaObject> metaObjectList = new LinkedList<>();
	protected ReflectorFactory reflectorFactory = new DefaultReflectorFactory();
	protected ObjectFactory objectFactory = new DefaultObjectFactory();
	protected ObjectWrapperFactory objectWrapperFactory = new DefaultObjectWrapperFactory();

	public FlowContextMap(FlowContext context) {
		super();
		this.context = context;
		super.put("$Attr", context.getAttributesMap());
		super.put("$Attributes", context.getAttributesMap());
		super.put("$Param", context.getParametersMap());
		super.put("$Parameters", context.getParametersMap());

	}

	@Override
	public Object put(String key, Object value) {
		if (StringUtils.startsWithAny(key, new String[] { "$Attr", "$Attributes", "$Param", "$Parameters" })) {
			throw new FlowException("key name must not start with $Attr, $Attributes, $Param, $Parameters");
		}
		return super.put(key, value);
	}

	@Override
	public Object get(Object key) {
		String strKey = (String) key;
		if (super.containsKey(strKey)) {
			return super.get(strKey);
		}
		if (CollectionUtils.isEmpty(metaObjectList)) {
			return null;
		}
		Object value = null;
		for (MetaObject metaObject : metaObjectList) {
			try {
				value = metaObject.getValue(strKey);
			} catch (Throwable e) {
				if (FlowUtils.getLogger().isDebugEnabled()) {
					FlowUtils.getLogger().debug("get value error!", e);
				}
			}
			if (value != null) {
				return value;
			}
		}
		return value;
	}

	public void addArgument(Object arg) {
		if(arg==null||arg.getClass().getName().startsWith("java.lang.")){
			return;
		}
		MetaObject metaObject = MetaObject.forObject(arg, objectFactory, objectWrapperFactory, reflectorFactory);
		metaObjectList.add(metaObject);
	}

}
