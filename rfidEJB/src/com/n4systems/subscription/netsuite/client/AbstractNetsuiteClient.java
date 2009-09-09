package com.n4systems.subscription.netsuite.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

public abstract class AbstractNetsuiteClient<K> {
	private static Logger logger = Logger.getLogger(AbstractNetsuiteClient.class);
	
	private final String password = "pickles";
	private final String netsuiteUrl = "https://forms.netsuite.com/app/site/hosting/scriptlet.nl?script=4&deploy=1&compid=723761&h=da3f7f1be20eb4e4299a";
	private final Class<K> returnClass;
	private final String taskName;
	private String requestMethod;
	private Map<String, String> requestParameters = new HashMap<String, String>();	
	private HttpsURLConnection connection;
	
	@SuppressWarnings("unchecked")
	private Map<String, Class> classMap = new HashMap<String, Class>();

	protected abstract void addRequestParameters();
	
	protected AbstractNetsuiteClient(Class<K> returnClass, String taskName) {
		this.returnClass = returnClass;
		this.taskName = taskName;
		this.requestMethod = "POST";
	}
	
	protected AbstractNetsuiteClient(Class<K> returnClass, String taskName, String requestMethod) {
		this(returnClass, taskName);
		this.requestMethod = requestMethod;
	}
		
	protected void addRequestParameter(String key, String value) {
		requestParameters.put(key, value);
	}
	
	private void addRequiredRequestParameters() {
		requestParameters.put("p", password);
		requestParameters.put("task", taskName);
	}
		
	private String constructRequestParametersString() {
		
		addRequiredRequestParameters();
		addRequestParameters();
		
		boolean firstOne = true;
		
		String requestString = "";
		for (String key : requestParameters.keySet()) {
			if (!firstOne) requestString+= "&";
			requestString += urlEncode(key, requestParameters.get(key));
			firstOne = false;
		}
		
		return requestString;
	}
	
	private void sendData(String data) throws IOException {
		
		DataOutputStream out = null;
		try {
			out = new DataOutputStream(connection.getOutputStream());
			out.writeBytes(data);
			out.flush();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
	
	private void createConnection() throws IOException {
		URL url = null;
		
		try {
			url = new URL(netsuiteUrl);
		} catch (MalformedURLException e) {

		}
		
		connection = (HttpsURLConnection) url.openConnection();
		connection.setRequestMethod(requestMethod);
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestProperty("User-Agent", "");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.connect();
	}
	
	private String getConnectionResponse() throws IOException {
		String response = "";
		String line;
		BufferedReader rd = null;
		try {
			rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while ((line = rd.readLine()) != null) {
				response += line;
			}
		} finally {
			if (rd != null) {
				rd.close();
			}
		}
		
		return response;
	}
	
	private String urlEncode(String key, String value) {
		String encoded = "";

		try {
			encoded = URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}

		return encoded;
	}
	
	public K execute() throws IOException {
		
		String response = "";
		
		try {
			String data = constructRequestParametersString();
	
			createConnection();
			
			logger.debug("Data to send: " + data);
	
			sendData(data);
			
			response = getConnectionResponse();
			
			logger.debug("Response in JSON was: "+response);
			
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

		
		return serializeJson(response);
	}
	
	private K serializeJson(String jsonResponse) {
		Gson gson = new Gson();
		
		K convertedObject = gson.fromJson(jsonResponse, returnClass);
		
		return convertedObject;		
	}
	
	@SuppressWarnings("unchecked")
	protected void addToClassMap(String name, Class clazz) {
		classMap.put(name, clazz);
	}
}
