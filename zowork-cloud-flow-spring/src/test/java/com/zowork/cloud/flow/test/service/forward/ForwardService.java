package com.zowork.cloud.flow.test.service.forward;

import com.zowork.cloud.flow.annotation.FlowParam;
import com.zowork.cloud.flow.annotation.FlowService;
@FlowService
public interface ForwardService {

	void forwardTest(@FlowParam("forward") String forward);
}
