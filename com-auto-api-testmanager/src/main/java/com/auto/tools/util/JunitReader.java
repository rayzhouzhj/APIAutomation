package com.auto.tools.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import com.auto.tools.context.Constants;
import com.auto.framework.utils.CommandPrompt;

public class JunitReader {

	private static JunitReader instance = null;
	private URLClassLoader classLoader = null;
	private String testJar = "." + File.separator + "libs" + File.separator + "AutoAPITest.jar";
	private String frameworkJar = "." + File.separator + "libs" + File.separator + "AutoExecutor.jar";

	@SuppressWarnings("resource")
	private JunitReader() throws IOException
	{
		try {
			System.out.println("Load Jar: " + testJar);
			System.out.println("Load Jar: " + frameworkJar);

			URL[] urls = {
					new URL("jar:file:" + testJar + "!/"),
					new URL("jar:file:" + frameworkJar + "!/")
			};
			this.classLoader = URLClassLoader.newInstance(urls, System.class.getClassLoader());

		} catch (IOException e) {
			throw e;
		}
	}

	public static JunitReader getInstance() throws IOException
	{
		if(instance == null)
		{
			instance = new JunitReader();
		}

		return instance;
	}

	public void extractTestCases() throws FileNotFoundException
	{
		String testcasePath = "." + File.separator + "com";
		FileHandler.getInstance().removeFile(new File(testcasePath));

		String command = "jar xvf " + testJar + " " + Constants.CLASS_FILE_PATH.substring(1);

		String os = System.getProperty("os.name");
		if(os.contains("Windows")) // if windows
		{
			command = "\"" + command + "\"";
		}
		
		System.out.println("Run Command: " + command);

		Process process;

		try
		{
			process = CommandPrompt.executeCommand(command);

			process.waitFor();
		}
		catch(FileNotFoundException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			// DO Nothing
		}
	}

	public List<String> getJunitTestCases(String classFullName)
	{
		List<String> list = new ArrayList<>();

		try {
			Class cls = this.classLoader.loadClass(classFullName);
			Method[] methods = cls.getDeclaredMethods();
			for(Method method : methods)
			{
				Annotation[] annotations = method.getAnnotations();
				for(Annotation annotation : annotations)
				{
					if(annotation.toString().contains("org.junit.Test"))
					{
						System.out.println("Find Junit Test: " + method.getName());
						list.add(method.getName());
					}
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return list;
	}
}
