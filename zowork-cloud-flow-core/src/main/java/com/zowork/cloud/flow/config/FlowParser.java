package com.zowork.cloud.flow.config;

import org.w3c.dom.Node;

import com.zowork.cloud.flow.node.FlowElement;

public interface FlowParser {

	FlowElement parse(Node parsedNode, ParseContext context);

}
