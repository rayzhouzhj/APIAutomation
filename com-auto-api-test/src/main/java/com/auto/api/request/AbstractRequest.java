package com.auto.api.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import com.auto.api.response.AbstractResponse;
import com.auto.framework.logs.AutoLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class AbstractRequest {

	public static String COOKIE = "";
	public static String HOST = "";
	public static String SCHEME = "https";

	private HttpRequestBase request;
	private int dealId;
	private String path = "";
	private List<NameValuePair> parameters;
	private AbstractResponse response;
	private static AutoLogger log = new AutoLogger("Request");
	
	public AbstractRequest(int id, String path, AbstractResponse response)
	{
//		log = new HandyLogger("Request");
		
		this.dealId = id;
		this.response = response;
		this.path = path;
		this.parameters = new ArrayList<NameValuePair>();
	}

	public abstract void initRequest();

	public AbstractRequest build() {
		HttpPost request = null;
		try {
			URIBuilder builder = new URIBuilder();
			builder.setScheme(SCHEME).setHost(HOST).setPath(path);
//			builder.addParameters(this.getParameters());

			URI uri = builder.build();

			// Create a method instance.
			request = new HttpPost(uri);
			request.addHeader("accept", "application/json");
			request.addHeader("Cookie", COOKIE);
			request.setEntity(new UrlEncodedFormEntity(this.getParameters()));

			this.setRequest(request);

		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return this;
	}

	public String sendRequest()
	{
		StringBuffer responseString = new StringBuffer();

		if(this.request == null)
		{
			throw new RuntimeException("Please build the request before sending out!");
		}

		log.info("Sending Request: " + request.getURI().toString());
		if(this.getParameters().size() > 0)
		{
			System.out.println("Request Parameters:");
			System.out.println("---------------------------------------");
			StringBuilder strBuilder = new StringBuilder();
			this.getParameters().forEach(
					a -> 
					strBuilder.append(a.getName() + ":" + a.getValue()).append("<br>\n")
					);
			log.info("Request Parameters:<br>\n" + strBuilder.toString());
			System.out.println("---------------------------------------");
		}
		else
		{
			log.info("Request Parameters: N/A");
		}

		try {
			// Create an instance of HttpClient.
			HttpClient httpClient = HttpClientBuilder.create().build();
			// Execute the method.
			HttpResponse httpResponse = httpClient.execute(this.request);

			if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				log.fatal("Send Request", "Method failed: " + httpResponse.getStatusLine());
			}

			BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

			String line = "";
			while ((line = rd.readLine()) != null) {
				responseString.append(line);
			}

			System.out.println("Response: " + responseString.toString());
		}
		catch (IOException e) {
			log.fatal("Send Request", "Fatal transport error: " + e.getMessage());
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		} finally {
			// Release the connection.
			request.releaseConnection();
		}

		return responseString.toString();
	}

	public <T> T sendRequest(Class<T> responseType)
	{
		String responseString = this.sendRequest();

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		T response = gson.fromJson(responseString, responseType);

		return response;
	}

	public HttpRequestBase getRequest() {
		return request;
	}

	public void setRequest(HttpRequestBase request) {
		this.request = request;
	}

	public int getDealId() {
		return dealId;
	}

	public void setDealId(int id) {
		this.dealId = id;
	}

	public void setPath(String path)
	{
		this.path = path;
	}
	public String getPath()
	{
		return this.path;
	}

	public List<NameValuePair> getParameters() {
		return parameters;
	}

	public void setParameters(List<NameValuePair> parameters) {
		this.parameters = parameters;
	}

	public void addParameter(String key, String value)
	{
		this.addParameter(new BasicNameValuePair(key, value));
	}

	public void addParameter(NameValuePair parameter)
	{
		boolean isExisted = false;
		int index = 0;
		for(; index < this.getParameters().size(); index++)
		{
			NameValuePair param = this.getParameters().get(index);
			if(param.getName().equals(parameter.getName()))
			{
				parameter = new BasicNameValuePair(param.getName(), parameter.getValue());
				isExisted = true;
				break;
			}
		}

		if(isExisted)
		{
			this.getParameters().set(index, parameter);
		}
		else
		{
			parameters.add(parameter);
		}
	}

	public void setResponse(AbstractResponse response)
	{
		this.response = response;
	}
	public AbstractResponse getResponse()
	{
		return this.response;
	}
}
