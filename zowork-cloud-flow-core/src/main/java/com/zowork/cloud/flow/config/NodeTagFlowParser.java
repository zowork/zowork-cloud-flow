/**
 *
 */
package com.zowork.cloud.flow.config;

import com.zowork.cloud.flow.node.FlowActionTagNode;
import com.zowork.cloud.flow.node.FlowElement;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

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
        String async = node.getAttribute("async");
        FlowActionTagNode actionNode = new FlowActionTagNode(context.getConfiguration(), id);
        ParseUtils.parseCommonAttribute(actionNode, node);
        actionNode.setClassName(className);
        actionNode.setRef(ref);
        actionNode.setFlowId(context.getFlowId());
        actionNode.setAsync(StringUtils.endsWithIgnoreCase("true", async));
        if (StringUtils.isNotBlank(test)) {
            actionNode.setTest(test);
        } else {
            actionNode.setTest("true");
        }
        context.registry(id, actionNode);
        return actionNode;
    }

}
