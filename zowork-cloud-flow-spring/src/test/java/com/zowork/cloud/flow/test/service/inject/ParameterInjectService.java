package com.zowork.cloud.flow.test.service.inject;

import java.util.Map;

import com.zowork.cloud.flow.annotation.FlowService;
import com.zowork.cloud.flow.test.param.PersonParam;

@FlowService
public interface ParameterInjectService {

	void injectTest(PersonParam param);
	
	void injectTest2(Map<String,Object> paramsMap);

}
