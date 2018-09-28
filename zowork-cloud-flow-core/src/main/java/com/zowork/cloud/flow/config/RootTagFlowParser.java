/**
 * 
 */
package com.zowork.cloud.flow.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.zowork.cloud.flow.FlowException;
import com.zowork.cloud.flow.node.FlowElement;
import com.zowork.cloud.flow.node.FlowsRootTagNode;

/**
 * @author luolishu
 *
 */
public class RootTagFlowParser implements FlowParser {
	static Logger logger = LoggerFactory.getLogger(RootTagFlowParser.class);

	@Override
	public FlowElement parse(Node parentNode, ParseContext context) {

		NodeList nodeList = parentNode.getChildNodes();
		if (nodeList.getLength() <= 0) {
			return null;
		}
		FlowsRootTagNode rootNode = new FlowsRootTagNode();
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
			String namespace = ((Element) parentNode).getAttribute("namespace");
			if (StringUtils.isBlank(namespace)) {
				throw new FlowException("5000",
						"tagname=" + node.getTagName() + " attribute [namespace] must not null!");
			}
			rootNode.setNamespace(namespace);
			((DefaultParseContext) context).setNamespace(namespace);
			FlowElement flow = flowParser.parse(node, context);
			rootNode.addChild(flow);
		}
		rootNode.setTest("true");
		return rootNode;
	}

}
