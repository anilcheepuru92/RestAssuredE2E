package com.qa.api.client;

import static io.restassured.RestAssured.expect;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.util.Base64;
import java.util.Map;

import com.qa.api.constants.AuthType;
import com.qa.api.exceptions.APIException;
import com.qa.api.manager.ConfigManager;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class RestClient {
	
	//define response specifications
	private ResponseSpecification responseSpec200 = expect().statusCode(200);
	private ResponseSpecification responseSpec200or201 = expect().statusCode(anyOf(equalTo(200), equalTo(201)));
	private ResponseSpecification responseSpec204 = expect().statusCode(204);
	private ResponseSpecification responseSpec200or404 = expect().statusCode(anyOf(equalTo(200), equalTo(404)));
	

	/**
	 * build request specifications
	 * @param baseUrl
	 * @param authType
	 * @param contentType
	 * @return RequestSpecification object with added headers
	 */
	private RequestSpecification setupRequest(String baseUrl, AuthType authType, ContentType contentType) {
		
		RequestSpecification reqSpec = RestAssured.given().log().all()
						.baseUri(baseUrl)
						.contentType(contentType)
						.accept(contentType);
		
		switch (authType) {
		case BEARER_TOKEN:
			reqSpec.header("Authorization", "Bearer "+ConfigManager.get("bearer_token"));
			break;
			
		case BASIC_AUTH:
			reqSpec.header("Authorization", "Basic "+generateBasicAuthToken());
			break;
			
		case API_KEY:
			reqSpec.header("X-goog-api-key", ConfigManager.get("api_key"));
			break;
			
		case NO_AUTH:
			System.out.println("Auth is not required...");
			break;

		default:
			System.out.println("This auth type is not supported. Please pass the right auth type...");
			throw new APIException("====== INVALID AUTHTYPE =====");
		}
		
		return reqSpec;
	}
	
	/**
	 * This method generates the Base64 encoded value using credentials
	 * @return BasicAuth Token in String format
	 */
	private String generateBasicAuthToken() {
		
		String username = ConfigManager.get("basicauth_username").trim();
		String password = ConfigManager.get("basicauth_password").trim();
		String credentials = username+":"+password; 
		
		//generate Base64 encoded value
		return Base64.getEncoder().encodeToString(credentials.getBytes());
	}
	
	
	/**
	 * apply path and query parameters
	 * @param request
	 * @param queryParams
	 * @param pathParams
	 */
	private void applyParams(RequestSpecification request, Map<String, String> queryParams, 
			Map<String, String> pathParams) {
		
		if(queryParams != null) {
			request.queryParams(queryParams);
		}
		if(pathParams != null) {
			request.pathParams(pathParams);
		}
		
	}

	
	/**
	 * This method is used to get resources
	 * @param baseUrl
	 * @param endPoint
	 * @param queryParams
	 * @param pathParams
	 * @param authType
	 * @param contentType
	 * @return GET API call response
	 */
	public Response get(String baseUrl, String endPoint, 
						Map<String, String> queryParams, 
						Map<String, String> pathParams, 
						AuthType authType, ContentType contentType) {
		
		RequestSpecification request = setupRequest(baseUrl, authType, contentType);
		applyParams(request, queryParams, pathParams);
		
		//Response response = request.get(endPoint).then().spec(responseSpec200or404).extract().response();
		Response response = request.get(endPoint).then().extract().response();
		response.prettyPrint();
		return response;
	}
	
	
	/**
	 * This method is used to create resources -- accepts any type of request body except a File
	 * @param <T>
	 * @param baseUrl
	 * @param endPoint
	 * @param body
	 * @param queryParams
	 * @param pathParams
	 * @param authType
	 * @param contentType
	 * @return POST API call response
	 */
	public <T>Response post(String baseUrl, String endPoint,
							T body, Map<String, String> queryParams, 
							Map<String, String> pathParams, 
							AuthType authType, ContentType contentType) {
		
		RequestSpecification request = setupRequest(baseUrl, authType, contentType);
		applyParams(request, queryParams, pathParams);
		
		//Response response = request.body(body).post(endPoint).then().spec(responseSpec200or201).extract().response();
		Response response = request.body(body).post(endPoint).then().extract().response();
		response.prettyPrint();
		return response;
	}
	
	public <T> Response post(String baseUrl, String endPoint, T body, Map<String, String> queryParams,
			Map<String, String> pathParams, AuthType authType, ContentType contentType, boolean enableUrlEncoding) {

		RequestSpecification request = setupRequest(baseUrl, authType, contentType);
		applyParams(request, queryParams, pathParams);
		
		//Response response = request.urlEncodingEnabled(enableUrlEncoding).body(body).post(endPoint).then().spec(responseSpec200or201).extract().response();
		Response response = request.urlEncodingEnabled(enableUrlEncoding).body(body).post(endPoint).then().extract().response();
		response.prettyPrint();
		return response;
	}
	
	
	/**
	 * This method is used to create resources -- accepts the request body as a File
	 * @param baseUrl
	 * @param endPoint
	 * @param body
	 * @param queryParams
	 * @param pathParams
	 * @param authType
	 * @param contentType
	 * @return POST API call response
	 */
	public Response post(String baseUrl, String endPoint,
							File body, Map<String, String> queryParams, 
							Map<String, String> pathParams, AuthType authType,
							ContentType contentType) {

		RequestSpecification request = setupRequest(baseUrl, authType, contentType);
		applyParams(request, queryParams, pathParams);

		//Response response = request.body(body).post(endPoint).then().spec(responseSpec200or201).extract().response();
		Response response = request.body(body).post(endPoint).then().extract().response();
		response.prettyPrint();
		return response;
	}
	

	/**
	 * This method is used to get the access token for OAuth2.0 based APIs
	 * @param baseUrl
	 * @param endPoint
	 * @param clientId
	 * @param clientSecret
	 * @param grantType
	 * @param contentType
	 * @return Response object with access token
	 */
	public Response post(String baseUrl, String endPoint, 
						String clientId, String clientSecret, 
						String grantType, ContentType contentType) {
		
		Response response = RestAssured.given()
											.contentType(contentType)
											.formParam("grant_type", grantType)
											.formParam("client_id", clientId)
											.formParam("client_secret", clientSecret)
										.when()
											.post(baseUrl+endPoint);

		response.prettyPrint();
		return response;
	}
	
	/**
	 * This method is used to call update resources entirely
	 * @param <T>
	 * @param baseUrl
	 * @param endPoint
	 * @param body
	 * @param queryParams
	 * @param pathParams
	 * @param authType
	 * @param contentType
	 * @return
	 */
	public <T> Response put(String baseUrl, String endPoint, 
							T body, Map<String, String> queryParams,
							Map<String, String> pathParams, AuthType authType, 
							ContentType contentType) {

		RequestSpecification request = setupRequest(baseUrl, authType, contentType);
		applyParams(request, queryParams, pathParams);

		//Response response = request.body(body).put(endPoint).then().spec(responseSpec200or201).extract().response();
		Response response = request.body(body).put(endPoint).then().extract().response();
		response.prettyPrint();
		return response;
	}
	
	
	/**
	 * This method is used to update resources partially
	 * @param <T>
	 * @param baseUrl
	 * @param endPoint
	 * @param body
	 * @param queryParams
	 * @param pathParams
	 * @param authType
	 * @param contentType
	 * @return
	 */
	public <T> Response patch(String baseUrl, String endPoint, 
								T body, Map<String, String> queryParams,
								Map<String, String> pathParams, AuthType authType, 
								ContentType contentType) {

		RequestSpecification request = setupRequest(baseUrl, authType, contentType);
		applyParams(request, queryParams, pathParams);

		//Response response = request.body(body).patch(endPoint).then().spec(responseSpec200or201).extract().response();
		Response response = request.body(body).patch(endPoint).then().extract().response();
		response.prettyPrint();
		return response;
	}
	
	
	/**
	 * This method is used to delete resources
	 * @param <T>
	 * @param baseUrl
	 * @param endPoint
	 * @param authType
	 * @param contentType
	 * @return
	 */
	public <T> Response delete(String baseUrl, String endPoint, 
								AuthType authType, 
								ContentType contentType) {

		RequestSpecification request = setupRequest(baseUrl, authType, contentType);

		//Response response = request.delete(endPoint).then().spec(responseSpec204).extract().response();
		Response response = request.delete(endPoint).then().extract().response();
		response.prettyPrint();
		return response;
	}
	
	
}
