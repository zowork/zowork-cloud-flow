/**
 * 
 */
package com.zowork.cloud.flow.config;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.zowork.cloud.flow.node.FlowElement;
import com.zowork.cloud.flow.node.FlowScriptTagNode;

/**
 * @author luolishu
 *
 */
public class ScriptTagFlowParser implements FlowParser {

	@Override
	public FlowElement parse(Node element, ParseContext context) {
		String script = element.getTextContent();
		FlowScriptTagNode scriptNode = new FlowScriptTagNode(context.getConfiguration(), script);
		ParseUtils.parseCommonAttribute(scriptNode, ((Element) element));
		return scriptNode;
	}

}
