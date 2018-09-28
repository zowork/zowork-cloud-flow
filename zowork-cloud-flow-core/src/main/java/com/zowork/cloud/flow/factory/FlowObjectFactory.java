package com.zowork.cloud.flow.factory;

import com.zowork.cloud.flow.node.FlowActionTagNode;

public interface FlowObjectFactory {

	Object getObject(FlowActionTagNode node);

}
