package com.auto.executor;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;

import com.auto.framework.test.TestContext;

public class TestExecutor {

	public static void main(String... args) throws Exception {

		TestContext.getInstance().setVariable("CMD_Mode", "ON");

		System.out.println("Argument Length: " + args.length);
		for(String arg : args)
		{
			System.out.println("Argument: " + arg);
		}
		
		String className = args[0];
		String methodName = args[1];
		int iteration = 1;
		try
		{
			iteration = Integer.parseInt(args[2]);
		}
		catch(Exception e)
		{
			System.out.println("ERROR: Argument[3]-Iteration [" + args[2] + "] is invalid, only number is accepted." );
			System.exit(0);
		}

		try
		{
			Class.forName(className);
		}
		catch(Exception e)
		{
			System.out.println("ERROR: Argument[1]-Class Name [" + className + "] cannot be found." );
			System.out.println(e);
			System.exit(0);
		}

		String[] vars;
		// set variables
		for(int i = 4; i <= args.length - 1; i++)
		{
			if(!args[i].contains("="))
			{
				System.out.println("ERROR: Argument [" + i + "] is invalid, argument should be in key-value pair, e.g. variableName=value" );
				System.exit(0);
			}
			vars = args[i].split("=");
			TestContext.getInstance().setVariable(vars[0], vars[1]);
		}


		
		for(int count = 0; count < iteration; count ++)
		{
			if("YES".equalsIgnoreCase(TestContext.getInstance().getVariable("QuitIteration")))
			{
				System.out.println("INFO: Quit Iteration.");
				break;
			}
			
			System.out.println("INFO: Starting testing TestClass [" + className + "] - Test Name [" + methodName + "], iteration " + (count + 1));
			try {
				Request request = Request.method(Class.forName(className), methodName);
				new JUnitCore().run(request);
			} catch (Exception e) {
				e.printStackTrace();
			}

			System.out.println("INFO: - Test Name [" + methodName + "], iteration " + (count + 1) + " completed.");
			
			// Wait for 5 seconds before starting next iteration
			Thread.sleep(5000);
		}

		System.exit(0);
	}

}
