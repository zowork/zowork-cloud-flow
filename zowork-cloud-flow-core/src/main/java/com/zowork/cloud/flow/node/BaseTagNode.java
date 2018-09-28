package com.zowork.cloud.flow.node;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.zowork.cloud.flow.FlowConfiguration;
import com.zowork.cloud.flow.FlowContext;

public abstract class BaseTagNode implements FlowElement {
	private static final long serialVersionUID = -8048151065777227280L;
	protected String id;
	protected String title;
	protected String name;
	protected String namespace;
	protected FlowConfiguration configuration;
	protected FlowElement parent;
	protected FlowElement prev;
	protected FlowElement next;
	protected String test;
	protected final List<FlowElement> childs = new LinkedList<>();
	protected String description;

	public boolean test(FlowContext context) {
		if (StringUtils.isBlank(test)) {
			return true;
		}
		try {
			return configuration.getExpressionValueResolver().resolveBoolean(test, context.getContextMap());
		} catch (Throwable e) {
			throw e;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public FlowConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(FlowConfiguration configuration) {
		this.configuration = configuration;
	}

	public FlowElement getParent() {
		return parent;
	}

	public void setParent(FlowElement parent) {
		this.parent = parent;
	}

	public FlowElement getPrev() {
		return prev;
	}

	public void setPrev(FlowElement prev) {
		this.prev = prev;
	}

	public FlowElement getNext() {
		return next;
	}

	public void setNext(FlowElement next) {
		this.next = next;
	}

	public List<FlowElement> getChilds() {
		return childs;
	}

	public List<FlowElement> childs() {
		return childs;
	}

	public void addChild(FlowElement child) {
		childs.add(child);
	}

	public boolean hasNext() {
		return this.next != null;
	}

	public boolean hasChild() {
		return CollectionUtils.isNotEmpty(childs);
	}

	public boolean hasParent() {
		return this.parent != null;
	}

	@Override
	public FlowElement parent() {
		return parent;
	}

	@Override
	public FlowElement prev() {
		return prev;
	}

	@Override
	public FlowElement next() {
		return next;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		ToStringBuilder builder=new ToStringBuilder(namespace);
		builder.append(this.getClass());
		builder.append("id="+id);
		builder.append("test="+test);
		builder.append(description);
		return builder.build();
	}

}
