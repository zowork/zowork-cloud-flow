package com.zowork.cloud.flow.test.service.componentscan;

import com.zowork.cloud.flow.annotation.FlowService;
import com.zowork.cloud.flow.test.model.PersonModel;
import com.zowork.cloud.flow.test.param.PersonParam;
@FlowService
public interface ComponentScanDemoFlowService1 {

	PersonModel helloworld(PersonParam param);

}
