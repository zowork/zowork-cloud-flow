package com.zowork.cloud.flow.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zowork.cloud.flow.node.FlowActionTagNode;

public class DefaultFlowObjectFactory implements FlowObjectFactory {
	protected static Logger logger = LoggerFactory.getLogger(DefaultFlowObjectFactory.class);

	@Override
	public Object getObject(FlowActionTagNode node) {
		if (node.getBeanClass() != null) {
			try {
				return node.getBeanClass().newInstance();
			} catch (Exception e) {
				logger.error("error create instance!", e);
			}
		}
		return null;
	}

}
