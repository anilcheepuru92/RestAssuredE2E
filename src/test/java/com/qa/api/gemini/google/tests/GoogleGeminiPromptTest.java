package com.qa.api.gemini.google.tests;

import org.testng.annotations.Test;

import com.qa.api.base.BaseTest;
import com.qa.api.constants.AuthType;
import com.qa.api.manager.ConfigManager;
import com.qa.api.utils.JsonPathValidatorUtil;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class GoogleGeminiPromptTest extends BaseTest {
	
	@Test(timeOut = 30000)
	public void testPromptWithGoogleGemini() {
		
		String requestBody = "{\r\n"
				+ "    \"contents\": [\r\n"
				+ "      {\r\n"
				+ "        \"parts\": [\r\n"
				+ "          {\r\n"
				+ "            \"text\": \"Explain about Andhra Pradesh in two sentences.\"\r\n"
				+ "          }\r\n"
				+ "        ]\r\n"
				+ "      }\r\n"
				+ "    ]\r\n"
				+ "  }";
				
		String apiKey = System.getenv("GEMINI_API_KEY");

		if (apiKey == null || apiKey.isBlank()) {
		    throw new IllegalStateException(
		        "GEMINI_API_KEY environment variable is not set"
		    );
		}

		ConfigManager.set("api_key", apiKey);
		
		Response response = restClient.post(BASE_URL_GOOGLE_GEMINI, GOOGLE_GEMINI_ENDPOINT, requestBody, null, null, AuthType.API_KEY, ContentType.JSON, false);
		String answer = JsonPathValidatorUtil.read(response, "$.candidates.[0].content.parts.[0].text");
		System.out.print("LLM answer => "+ answer+"\n");
	}

}
