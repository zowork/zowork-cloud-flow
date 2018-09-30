package com.zowork.cloud.flow.test.service.script;

import com.zowork.cloud.flow.annotation.FlowParam;
import com.zowork.cloud.flow.annotation.FlowService;

@FlowService
public interface ScriptService {

	void testScript(@FlowParam("forward") String forward);

}
