package com.qa.api.gorest.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.qa.api.base.BaseTest;
import com.qa.api.constants.AuthType;
import com.qa.api.manager.ConfigManager;
import com.qa.api.pojo.User;
import com.qa.api.utils.StringUtil;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class UpdateUserTest extends BaseTest{
	
	@BeforeClass
	public void goRestTokenSetup() {
		ConfigManager.set("bearer_token", "10c0ba53681b541d08ba132593d8f32c28b45c81fd7717b42cc1f833144f01c2");
	}
	
	@Test
	public void updateUser() {
		
		//1. Create a new user
		String emailId = StringUtil.getRandomEmail();
		User user = new User(null, "Natasha Romanoff", emailId, "female", "active"); 
		
		Response responsePost = restClient.post(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, user, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		int userId = responsePost.jsonPath().getInt("id");
		System.out.println("Created UserID:"+userId);
		
		//2. Fetch the same user
		Response responseGet = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT+"/"+userId, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(responseGet.statusCode(), 200);
		
		//3. Update the user
		user.setName("Natasha Randolf");
		user.setStatus("inactive");
		Response responsePut = restClient.put(BASE_URL_GOREST, GOREST_USERS_ENDPOINT+"/"+userId, user, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(responsePut.jsonPath().getString("status"), user.getStatus());
		Assert.assertEquals(responsePut.jsonPath().getString("name"), user.getName());
		
	}

}
