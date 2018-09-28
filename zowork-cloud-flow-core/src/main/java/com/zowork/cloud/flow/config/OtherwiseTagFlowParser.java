/**
 * 
 */
package com.zowork.cloud.flow.config;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.zowork.cloud.flow.node.FlowElement;
import com.zowork.cloud.flow.node.FlowIfTagNode;

/**
 * @author luolishu
 *
 */
public class OtherwiseTagFlowParser implements FlowParser {
	static Logger logger = LoggerFactory.getLogger(OtherwiseTagFlowParser.class);

	@Override
	public FlowElement parse(Node parentNode, ParseContext context) {
		if (parentNode == null) {
			return null;
		}
		NodeList nodeList = parentNode.getChildNodes();
		if (nodeList.getLength() <= 0) {
			return null;
		}
		String test = ((Element) parentNode).getAttribute("test");

		List<FlowElement> childList = new LinkedList<>();

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node item = nodeList.item(i);
			if (!(item instanceof Element)) {
				continue;
			}
			Element node = (Element) item;
			FlowParser flowParser = FlowParserFactory.create(node);
			if (flowParser == null) {
				logger.error("flowParser is null!node=" + node);
				continue;
			}
			FlowElement parseNode = flowParser.parse(node, context);
			childList.add(parseNode);
		}

		FlowIfTagNode ifNode = new FlowIfTagNode(context.getConfiguration(), test, childList);
		ParseUtils.parseCommonAttribute(ifNode, ((Element) parentNode));
		ifNode.setTest("");
		ParseUtils.initRelation(childList, ifNode, context);
		return ifNode;
	}

}
