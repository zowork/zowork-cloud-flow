/**
 * 
 */
package com.zowork.cloud.flow.config;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.zowork.cloud.flow.node.FlowChooseTagNode;
import com.zowork.cloud.flow.node.FlowElement;

/**
 * @author luolishu
 *
 */
public class ChooseTagFlowParser implements FlowParser {
	static Logger logger = LoggerFactory.getLogger(ChooseTagFlowParser.class);

	@Override
	public FlowElement parse(Node parentNode, ParseContext context) {
		if (parentNode == null) {
			return null;
		}
		NodeList nodeList = parentNode.getChildNodes();
		if (nodeList.getLength() <= 0) {
			return null;
		}

		List<FlowElement> ifNodeList = new LinkedList<>();
		FlowElement defaultNode = null;
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
			if (StringUtils.equals("when", node.getTagName())) {
				FlowElement parseNode = flowParser.parse(node, context);
				ifNodeList.add(parseNode);
			}
			if (StringUtils.equals("otherwise", node.getTagName())) {
				defaultNode = flowParser.parse(node, context);

			}

		}

		FlowChooseTagNode chooseNode = new FlowChooseTagNode(context.getConfiguration(), ifNodeList, defaultNode);
		chooseNode.setTest("true");
		ParseUtils.parseCommonAttribute(chooseNode, (Element) parentNode);
		/**
		 * 设置兄弟父子关系
		 */
		ParseUtils.initRelation(ifNodeList, chooseNode, context);
		if (CollectionUtils.isNotEmpty(ifNodeList)) {
			FlowElement lastIf = ifNodeList.get(ifNodeList.size() - 1);
			lastIf.setNext(defaultNode);
			if (defaultNode != null) {
				defaultNode.setParent(chooseNode);
				defaultNode.setPrev(lastIf);
			}
		}
		if (defaultNode != null) {
			defaultNode.setParent(chooseNode);
		}
		return chooseNode;
	}

}
