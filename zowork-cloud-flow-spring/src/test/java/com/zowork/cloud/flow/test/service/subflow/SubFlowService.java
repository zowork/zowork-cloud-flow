package com.zowork.cloud.flow.test.service.subflow;

import com.zowork.cloud.flow.annotation.FlowParam;
import com.zowork.cloud.flow.annotation.FlowService;

@FlowService
public interface SubFlowService {

	void subFlowTest(@FlowParam("channel")String channel);
}
