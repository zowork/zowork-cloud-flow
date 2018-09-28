package com.zowork.cloud.flow.test.service.inject;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zowork.cloud.flow.test.param.PersonParam;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = "classpath:applicationContext.flow.test.xml")
public class ParameterInjectTest {
	@Resource
	ParameterInjectService parameterInjectService;

	@Test
	public void testInject() {
		PersonParam param = new PersonParam();
		param.setCity("Hello world");
		parameterInjectService.injectTest(param);
	}

	@Test
	public void testInject2() {
		Map<String, Object> paramsMap = new LinkedHashMap<>();
		paramsMap.put("city", "Hello world");
		paramsMap.put("age", 2D);
		parameterInjectService.injectTest2(paramsMap);
	}
}
