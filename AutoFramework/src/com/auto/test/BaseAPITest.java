package com.auto.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.rules.TestName;

public class BaseAPITest  extends BaseTest{

	@Before
	public void setUp() throws Exception {
		System.out.println("INFO: Setting up test environment...");
		initilize();
		
		info("Start Testing");
	}

	@After
	public void tearDown() throws Exception {

		System.out.println("INFO: Tearing down test environment...");

		if("Failed".equalsIgnoreCase(TestContext.getInstance().getVariable("Status")))
		{
			Assert.fail();
		}

	}

	public BaseAPITest()
	{
		name = new TestName();
	}
}
