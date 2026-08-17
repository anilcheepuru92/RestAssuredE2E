package com.qa.api.schemavalidation.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.qa.api.base.BaseTest;
import com.qa.api.constants.AuthType;
import com.qa.api.manager.ConfigManager;
import com.qa.api.pojo.User;
import com.qa.api.utils.SchemaValidatorUtil;
import com.qa.api.utils.StringUtil;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class GoRestSchemaTest extends BaseTest{
	
	@BeforeClass
	public void goRestTokenSetup() {
		ConfigManager.set("bearer_token", "10c0ba53681b541d08ba132593d8f32c28b45c81fd7717b42cc1f833144f01c2");
	}
	
	@Test
	public void verifySchemaForGetUsers() {
		
		Response response = restClient.get(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, null, null, AuthType.BEARER_TOKEN, ContentType.ANY);
		Assert.assertTrue(SchemaValidatorUtil.validateSchema(response, "getuserschema.json"));
	}
	
	
	@Test
	public void verifySchemaForCreateUser() {
		
		String emailId = StringUtil.getRandomEmail();
		User user = new User(null, "Bruce Banner", emailId, "male", "inactive"); 
		
		Response response = restClient.post(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, user, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertTrue(SchemaValidatorUtil.validateSchema(response, "createuserschema.json"));
	}

}
