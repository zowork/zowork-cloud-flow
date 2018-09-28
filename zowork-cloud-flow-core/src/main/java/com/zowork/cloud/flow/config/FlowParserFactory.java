package com.zowork.cloud.flow.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.w3c.dom.Node;

public class FlowParserFactory {
	static Map<String, FlowParser> PARSER_MAP = new ConcurrentHashMap<>();
	static {
		PARSER_MAP.put("flows", new RootTagFlowParser());
		PARSER_MAP.put("flow", new FlowTagFlowParser());
		PARSER_MAP.put("choose", new ChooseTagFlowParser());
		PARSER_MAP.put("sub-flow", new SubFlowTagFlowParser());
		PARSER_MAP.put("node", new NodeTagFlowParser());
		PARSER_MAP.put("script", new ScriptTagFlowParser());
		PARSER_MAP.put("when", new IfTagFlowParser());
		PARSER_MAP.put("otherwise", new OtherwiseTagFlowParser()); 
		PARSER_MAP.put("if", new IfTagFlowParser());
		PARSER_MAP.put("goto", new GotoTagFlowParser());
	}

	public static FlowParser create(Node node) {
		String nodeTagName = node.getNodeName();
		return PARSER_MAP.get(nodeTagName);
	}
}
