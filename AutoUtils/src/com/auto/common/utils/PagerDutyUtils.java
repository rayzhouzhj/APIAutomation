package com.auto.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class PagerDutyUtils {

	public static void updateToPagerDuty(String url, String description)
	{
		HttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead 
		HttpPost request = null;

		try 
		{
			request = new HttpPost(url);
			String requestBody = ResourceReader.readResource("resources" + File.separator + "pagerduty.json");
			requestBody = requestBody.replace("${DESCRIPTION}", description);
			StringEntity params =new StringEntity(requestBody);
			request.addHeader("content-type", "application/x-www-form-urlencoded");
			request.setEntity(params);
			HttpResponse httpResponse = httpClient.execute(request);

			BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

			String line = "";
			StringBuilder responseString = new StringBuilder();
			while ((line = rd.readLine()) != null) {
				responseString.append(line);
			}

			System.out.println("Response: " + responseString.toString());

		}catch (Exception ex) {

			ex.printStackTrace();

		} finally {
			if(request != null)
			{
				request.releaseConnection();
			}
		}
	}
}
