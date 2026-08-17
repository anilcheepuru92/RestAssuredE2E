package com.qa.api.gorest.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.qa.api.base.BaseTest;
import com.qa.api.constants.AuthType;
import com.qa.api.manager.ConfigManager;
import com.qa.api.pojo.User;
import com.qa.api.utils.ObjectMapperUtil;
import com.qa.api.utils.StringUtil;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class DeserializationTest extends BaseTest{
	
	@BeforeClass
	public void goRestTokenSetup() {
		ConfigManager.set("bearer_token", "10c0ba53681b541d08ba132593d8f32c28b45c81fd7717b42cc1f833144f01c2");
	}
	
	@Test
	public void getUserAndDeserialize() {
		
		//1.POST
		String emailId = StringUtil.getRandomEmail();
		User user = new User(null, "Scarlet Witch", emailId, "male", "active"); 
		
		Response response = restClient.post(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, user, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(response.jsonPath().getString("name"), user.getName());
		
		int id = response.jsonPath().getInt("id");
		System.out.println("New User ID: "+ id);
		
		//2.GET
		response = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT+"/"+id, null, null, AuthType.BEARER_TOKEN, ContentType.ANY);
		
		//mapping response JSON to the POJO class
		User userResponse = ObjectMapperUtil.deserialize(response, user.getClass());
		
		Assert.assertEquals(userResponse.getId(), id);
		Assert.assertEquals(userResponse.getName(), user.getName());
		Assert.assertEquals(userResponse.getEmail(), user.getEmail());
		Assert.assertEquals(userResponse.getGender(), user.getGender());
		Assert.assertEquals(userResponse.getStatus(), user.getStatus());
	}

}
