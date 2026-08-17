package com.qa.api.gorest.tests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.qa.api.base.BaseTest;
import com.qa.api.constants.AuthType;
import com.qa.api.manager.ConfigManager;
import com.qa.api.utils.JsonPathValidatorUtil;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class GetUserTest extends BaseTest{
	
	private String userID;
	
	@BeforeClass
	public void goRestTokenSetup() {
		ConfigManager.set("bearer_token", "10c0ba53681b541d08ba132593d8f32c28b45c81fd7717b42cc1f833144f01c2");
	}
	
	@Test(priority = 1)
	public void getAllUsers() {
		
		Response response = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(response.statusCode(), 200);
		Assert.assertTrue(response.statusLine().contains("OK"));	
		
		/*
		 * // User[] users = ObjectMapperUtil.deserialize(response, User[].class); //
		 * System.out.println(users[0].getId());
		 */
		
		List<Number> userIDList = JsonPathValidatorUtil.readList(response, "$.*.id");
		userID = ""+userIDList.get(0);
		System.out.println("FIRST USERID ==> "+userID);
		
	}
	
	@Test(priority = 2)
	public void getAllUsersWithQueryParams() {
		
		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap.put("gender", "male");
		queryMap.put("status", "inactive");
		
		Response response = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, queryMap, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(response.statusCode(), 200);
		Assert.assertTrue(response.statusLine().contains("OK"));
		
	}
	
	@Test(priority = 3)
	public void getSingleUser() {
		//userID="0";
		Response response = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT+"/"+userID, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(response.statusCode(), 200);
		Assert.assertTrue(response.statusLine().contains("OK"));
		
	}

}
