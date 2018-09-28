/**
 * 
 */
package com.zowork.cloud.flow.config;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.zowork.cloud.flow.node.FlowElement;
import com.zowork.cloud.flow.node.FlowGotoTagNode;

/**
 * @author luolishu
 *
 */
public class GotoTagFlowParser implements FlowParser {

	@Override
	public FlowElement parse(Node element, ParseContext context) {
		Element node = (Element) element;
		String id = node.getAttribute("id");
		String flowId = node.getAttribute("flow-id");
		FlowGotoTagNode gotoNode = new FlowGotoTagNode(context.getConfiguration(), id, flowId);
		ParseUtils.parseCommonAttribute(gotoNode, ((Element) element));
		return gotoNode;
	}

}
