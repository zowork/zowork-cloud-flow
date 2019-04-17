package com.zowork.cloud.flow.logic;

import com.zowork.cloud.flow.FlowContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoLogic {
    static Logger logger = LoggerFactory.getLogger(EchoLogic.class);

    public void execute(FlowContext context) {
        if (logger.isDebugEnabled()) {
            logger.debug("invoke flow namespace=" + context.getNamespace() + ",flowId=" + context.getFlowId());
        }
    }
}
