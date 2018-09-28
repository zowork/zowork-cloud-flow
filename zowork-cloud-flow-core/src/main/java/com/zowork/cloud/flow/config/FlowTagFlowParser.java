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
import com.zowork.cloud.flow.node.FlowTagNode;

/**
 * @author luolishu
 *
 */
public class FlowTagFlowParser implements FlowParser {
	static Logger logger = LoggerFactory.getLogger(FlowTagFlowParser.class);

	@Override
	public FlowElement parse(Node parentNode, ParseContext context) {
		if (parentNode == null) {
			return null;
		}
		NodeList nodeList = parentNode.getChildNodes();
		if (nodeList.getLength() <= 0) {
			return null;
		}
		String id = ((Element) parentNode).getAttribute("id");
		if (StringUtils.isBlank(id)) {
			throw new FlowException("5000",
					"tagname=" + ((Element) parentNode).getTagName() + " attribute [id] must not null!");
		}
		((DefaultParseContext) context).setFlowId(id);
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

		FlowTagNode flowNode = new FlowTagNode(id, childList, context.getConfiguration());
		ParseUtils.parseCommonAttribute(flowNode, ((Element) parentNode));
		ParseUtils.initRelation(childList, flowNode, context);
		flowNode.setTest("true");
		context.getConfiguration().registerFlow(context.getNamespace(), id, flowNode);
		return flowNode;
	}

}
