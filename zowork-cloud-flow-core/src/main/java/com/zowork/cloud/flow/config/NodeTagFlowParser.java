/**
 * 
 */
package com.zowork.cloud.flow.config;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.zowork.cloud.flow.node.FlowActionTagNode;
import com.zowork.cloud.flow.node.FlowElement;

/**
 * @author luolishu
 *
 */
public class NodeTagFlowParser implements FlowParser {

	@Override
	public FlowElement parse(Node element, ParseContext context) {
		Element node = (Element) element;
		String id = node.getAttribute("id");
		String className = node.getAttribute("class");
		String ref = node.getAttribute("ref");
		String test = node.getAttribute("if");
		FlowActionTagNode actionNode = new FlowActionTagNode(context.getConfiguration(), id);
		ParseUtils.parseCommonAttribute(actionNode, node);
		actionNode.setClassName(className);
		actionNode.setRef(ref);
		if (StringUtils.isNotBlank(test)) {
			actionNode.setTest(test);
		} else {
			actionNode.setTest("true");
		}
		context.registry(id, actionNode);
		return actionNode;
	}

}
