package com.zowork.cloud.flow.spring.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = "classpath:applicationContext.flow.test.xml")
public class SpringIntegrationTest {
    @Test
	public void testConfig(){
		System.out.println("test");
	}
}
