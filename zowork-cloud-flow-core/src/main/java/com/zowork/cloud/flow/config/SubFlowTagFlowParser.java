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
import com.zowork.cloud.flow.node.FlowSubFlowTagNode;

/**
 * @author luolishu
 *
 */
public class SubFlowTagFlowParser implements FlowParser {
	static Logger logger = LoggerFactory.getLogger(SubFlowTagFlowParser.class);

	@Override
	public FlowElement parse(Node parentNode, ParseContext context) {
		if (parentNode == null) {
			return null;
		}
		
		String id = ((Element) parentNode).getAttribute("id");
		String refFlowId = ((Element) parentNode).getAttribute("ref-flow-id");
		NodeList nodeList = parentNode.getChildNodes();
		if (nodeList.getLength() <= 0&&StringUtils.isBlank(refFlowId)) {
			return null;
		}
		if (StringUtils.isBlank(id)&&StringUtils.isBlank(refFlowId)) {
			throw new FlowException("5000",
					"tagname=" + ((Element) parentNode).getTagName() + " attribute [id] must not null!");
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
			childList.add(parseNode);
		}

		FlowSubFlowTagNode subFlowNode = new FlowSubFlowTagNode(context.getConfiguration(), id, refFlowId, childList);
		ParseUtils.parseCommonAttribute(subFlowNode, ((Element) parentNode));
		ParseUtils.initRelation(childList, subFlowNode, context);
		subFlowNode.setTest("true");
		return subFlowNode;
	}

}
