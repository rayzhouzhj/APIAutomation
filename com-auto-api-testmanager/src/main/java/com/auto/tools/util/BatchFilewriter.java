package com.auto.tools.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.auto.tools.models.TestCase;

public class BatchFilewriter {

	public static void writeStartUpBatchFile(TestCase testCase)
	{
		FileWriter fw;
		try {
			fw = new FileWriter("O:\\EBS ITT\\Ray\\TestJar\\BatchRunTool\\start.bat");
			
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write("cd /d \"C:\\OracleATS\\openScript\"\n\r");
//			bw.write("set RT_NAME=\"" + testCase.getDriverName() + "\"\n\r");
//			bw.write("set PROP_FILE=\"" + testCase.getPropFile() + "\"\n\r");
//			bw.write("set PHP_URL=\"" + testCase.get_php_url() + "\"\n\r");
//			bw.write("set JSP_URL=\"" + testCase.get_jsp_url() + "\"\n\r");
//			bw.write("set ISTORE_URL=\"" + testCase.get_istore_url() + "\"\n\r");
			bw.write("runScript.bat %RT_NAME% "
					+ "-propertiesPath %PROP_FILE% "
					+ "-oracle_php_url %PHP_URL% "
					+ "-oracle_jsp_url %JSP_URL% "
					+ "-oracle_istore_url %ISTORE_URL% "
					+ "-batchmode ON\n\r");
			
			bw.flush();
			fw.close();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
