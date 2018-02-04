package com.testmydata.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.microsoft.tfs.core.httpclient.HttpClient;

public class TestClassforAnantha {
	static HttpClient client = null;

	public static void main(String[] args) {
		TestClassforAnantha ts = new TestClassforAnantha();
		ts.gettestplans();
	}

	public HttpClient getresttfscon() {
		client = new HttpClient();

		// client.BaseAddress = new
		// Uri("https://{accountname}.visualstudio.com");
		// client.DefaultRequestHeaders.Accept.Clear();
		// client.DefaultRequestHeaders.Accept
		// .Add(new
		// System.Net.Http.Headers.MediaTypeWithQualityHeaderValue("application/json"));
		// client.DefaultRequestHeaders.Authorization = new
		// AuthenticationHeaderValue("Basic", credentials);

		return client;
	}

	public void gettestplans() {
		try {

			URL url = new URL(
					"https://jettysoft.visualstudio.com/DefaultCollection/Parmzpizza_Redesign/_apis/test/plans/1/suites/1/testcases?api-version=2.0");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("count", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
	}

	public void gettestsuites() {

	}

	public void gettestcases() {

	}
}
