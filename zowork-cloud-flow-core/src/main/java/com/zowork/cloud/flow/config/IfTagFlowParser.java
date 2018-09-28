/**
 * 
 */
package com.zowork.cloud.flow.config;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.zowork.cloud.flow.FlowException;
import com.zowork.cloud.flow.node.FlowElement;
import com.zowork.cloud.flow.node.FlowIfTagNode;

/**
 * @author luolishu
 *
 */
public class IfTagFlowParser implements FlowParser {
	static Logger logger = LoggerFactory.getLogger(IfTagFlowParser.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zowork.cloud.flow.config.FlowParser#parse(org.w3c.dom.Element)
	 */
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
		if (StringUtils.isBlank(test)) {
			throw new FlowException("5000",
					"tagname=<" + ((Element) parentNode).getTagName() + "> attribute [test] must not null!");
		}

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
			if (parseNode == null) {
				throw new FlowException("5000", "parse error!node=" + node);
			}
			childList.add(parseNode);
		}

		FlowIfTagNode ifNode = new FlowIfTagNode(context.getConfiguration(), test, childList);
		ParseUtils.parseCommonAttribute(ifNode, ((Element) parentNode));
		ParseUtils.initRelation(childList, ifNode, context);
		return ifNode;
	}

}
