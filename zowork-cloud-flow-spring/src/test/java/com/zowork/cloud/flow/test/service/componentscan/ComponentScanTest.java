package com.zowork.cloud.flow.test.service.componentscan;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zowork.cloud.flow.test.model.PersonModel;
import com.zowork.cloud.flow.test.param.PersonParam;
import com.zowork.cloud.flow.test.param.PersonParam.Color;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = "classpath:applicationContext.flow.test.xml")
public class ComponentScanTest {
	@Resource
	ComponentScanDemoFlowService1 demoService;

	@Test
	public void testComponentScan() {
		Assert.assertNotNull(demoService);
		PersonParam param = new PersonParam();
		param.setAge(11);
		PersonModel model = demoService.helloworld(param);
		Assert.assertEquals(model.getRegion(), "未知区域");
		Assert.assertEquals(model.getName(), "kid");
		param.setCity("BJ");
		model = demoService.helloworld(param);
		Assert.assertEquals(model.getRegion(), "北方人");
		Assert.assertEquals(model.getName(), "kid");
		param.setCity("GD");
		model = demoService.helloworld(param);
		Assert.assertEquals(model.getRegion(), "南方人");
		Assert.assertEquals(model.getName(), "kid");
		param.setCity("SH");
		model = demoService.helloworld(param);
		Assert.assertEquals(model.getRegion(), "未知区域");
		Assert.assertEquals(model.getName(), "kid");
		param.setCity("GD");
		param.setColor(Color.BLACK);
		model = demoService.helloworld(param);
		Assert.assertEquals(model.getRegion(), "南方人");
		Assert.assertEquals(model.getName(), "kid");
		Assert.assertEquals(model.getColorName(), "黑人");
		param.setCity("GD");
		param.setColor(Color.WHITE);
		model = demoService.helloworld(param);
		Assert.assertEquals(model.getRegion(), "南方人");
		Assert.assertEquals(model.getName(), "kid");
		Assert.assertEquals(model.getColorName(), "白人");
		param.setCity("SH");
		param.setColor(Color.WHITE);
		model = demoService.helloworld(param);
		Assert.assertEquals(model.getRegion(), "未知区域");
		Assert.assertEquals(model.getName(), "kid");
		Assert.assertEquals(model.getColorName(), null);
		param.setCity("BJ");
		param.setColor(Color.WHITE);
		model = demoService.helloworld(param);
		Assert.assertEquals(model.getRegion(), "北方人");
		Assert.assertEquals(model.getName(), "kid");
		Assert.assertEquals(model.getColorName(), null);
		param.setAge(18);
		param.setCity("BJ");
		param.setColor(Color.WHITE);
		model = demoService.helloworld(param);
		Assert.assertEquals(model.getRegion(), null);
		Assert.assertEquals(model.getName(), "young");
		Assert.assertEquals(model.getColorName(), null);
		Assert.assertEquals(model.getGroup(), "小年轻");
		param.setAge(20);
		param.setCity("BJ");
		param.setColor(Color.WHITE);
		model = demoService.helloworld(param);
		Assert.assertEquals(model.getRegion(), null);
		Assert.assertEquals(model.getName(), "young");
		Assert.assertEquals(model.getColorName(), null);
		Assert.assertEquals(model.getGroup(), "弱冠");
		param.setAge(29);
		param.setCity("BJ");
		param.setColor(Color.WHITE);
		model = demoService.helloworld(param);
		Assert.assertEquals(model.getRegion(), null);
		Assert.assertEquals(model.getName(), "young");
		Assert.assertEquals(model.getColorName(), null);
		Assert.assertEquals(model.getGroup(), "弱冠");
		param.setAge(30);
		param.setCity("BJ");
		param.setColor(Color.WHITE);
		model = demoService.helloworld(param);
		Assert.assertEquals(model.getRegion(), null);
		Assert.assertEquals(model.getName(), "young");
		Assert.assertEquals(model.getColorName(), null);
		Assert.assertEquals(model.getGroup(), "而立");
		param.setAge(40);
		param.setCity("BJ");
		param.setColor(Color.WHITE);
		model = demoService.helloworld(param);
		Assert.assertEquals(model.getRegion(), null);
		Assert.assertEquals(model.getName(), "young");
		Assert.assertEquals(model.getColorName(), null);
		Assert.assertEquals(model.getGroup(), "不惑");
		param.setAge(50);
		param.setCity("BJ");
		param.setColor(Color.WHITE);
		model = demoService.helloworld(param);
		Assert.assertEquals(model.getRegion(), null);
		Assert.assertEquals(model.getName(), "young");
		Assert.assertEquals(model.getColorName(), null);
		Assert.assertEquals(model.getGroup(), "知天命");
		param.setAge(60);
		param.setCity("BJ");
		param.setColor(Color.WHITE);
		model = demoService.helloworld(param);
		Assert.assertEquals(model.getRegion(), null);
		Assert.assertEquals(model.getName(), "young");
		Assert.assertEquals(model.getColorName(), null);
		Assert.assertEquals(model.getGroup(), "知天命");
		
		param.setAge(70);
		param.setCity("BJ");
		param.setColor(Color.WHITE);
		model = demoService.helloworld(param);
		Assert.assertEquals(model.getRegion(), null);
		Assert.assertEquals(model.getName(), "old");
		Assert.assertEquals(model.getColorName(), null);
		Assert.assertEquals(model.getGroup(), null);
	}
}
