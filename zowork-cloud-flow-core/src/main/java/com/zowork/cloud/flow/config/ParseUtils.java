package com.zowork.cloud.flow.config;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.zowork.cloud.flow.FlowException;
import com.zowork.cloud.flow.node.BaseTagNode;
import com.zowork.cloud.flow.node.FlowElement;

public class ParseUtils {

	public static void initRelation(List<FlowElement> childList, FlowElement parent, ParseContext context) {
		/**
		 * 设置前后关系
		 */
		for (int i = 0; i < childList.size(); i++) {
			FlowElement prev = null;
			FlowElement next = null;
			FlowElement item = childList.get(i);
			if (item == null) {
				throw new FlowException("5000", "childList error!");
			}
			if (i > 0) {
				prev = childList.get(i - 1);
			}
			if (i < (childList.size() - 1)) {
				next = childList.get(i + 1);
			}

			item.setParent(parent);
			item.setPrev(prev);
			item.setNext(next);
		}
	}

	public static String getAttribute(Element el, String name) {
		return StringUtils.trim(el.getAttribute(name));
	}

	public static String getDescription(Element el) {
		if (el.hasAttribute("description")) {
			return StringUtils.trim(el.getAttribute("description"));
		}
		NodeList childList = el.getElementsByTagName("description");
		if (childList.getLength() > 0) {
			return childList.item(0).getTextContent();
		}
		return null;
	}

	public static void parseCommonAttribute(BaseTagNode node, Element el) {
		String attr = getAttribute(el, "id");
		node.setId(attr);
		attr = getAttribute(el, "name");
		node.setName(attr);
		attr = getAttribute(el, "namespace");
		node.setNamespace(attr);
		attr = getAttribute(el, "title");
		node.setTitle(attr);
		node.setDescription(getDescription(el));
	}

}
