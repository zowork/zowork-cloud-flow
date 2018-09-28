package com.zowork.cloud.flow.node;

import java.io.Serializable;
import java.util.List;

import com.zowork.cloud.flow.FlowConfiguration;
import com.zowork.cloud.flow.FlowContext;

public interface FlowElement extends Serializable {
	boolean test(FlowContext context);

	String getId();

	/**
	 * 父节点
	 * 
	 * @return
	 */
	FlowElement parent();

	/**
	 * 相同层级的前一个节点
	 * 
	 * @return
	 */
	FlowElement prev();
	void setParent(FlowElement parent);
	void setPrev(FlowElement prev);

	/**
	 * 下一个节点
	 * 
	 * @return
	 */
	FlowElement next();
	void setNext(FlowElement next);
	boolean hasNext();

	boolean hasChild();

	boolean hasParent();

	/**
	 * 子节点
	 * 
	 * @return
	 */
	List<FlowElement> childs();

	/**
	 * 添加子节点
	 * 
	 * @param child
	 */
	void addChild(FlowElement child);

	FlowConfiguration getConfiguration();

}
