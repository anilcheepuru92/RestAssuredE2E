package com.qa.api.gorest.tests;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.chaintest.plugins.ChainTestListener;
import com.qa.api.base.BaseTest;
import com.qa.api.constants.AuthType;
import com.qa.api.manager.ConfigManager;
import com.qa.api.pojo.User;
import com.qa.api.utils.CSVUtil;
import com.qa.api.utils.ExcelUtil;
import com.qa.api.utils.StringUtil;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@Epic("US 199 -- Create a GoRest user")
@Feature("US 47 -- Create user with a POST call")
@Story("US 236 -- Create a GoRest user with JSON, POJO and a file")
public class CreateUserTest extends BaseTest{
	
	@Severity(SeverityLevel.MINOR)
	@Test
	public void createUser() {
		String tempEmail = StringUtil.getRandomEmail();
		String emailId = tempEmail.replace(tempEmail.charAt(0), 'A');
		String userJson = "{\r\n"
				+ "    \"name\": \"Suryakantam Saluri\",\r\n"
				+ "\"email\":\""+emailId+"\",\r\n"
				+ "    \"gender\": \"female\",\r\n"
				+ "    \"status\": \"inactive\"\r\n"
				+ "}";
		
		Response response = restClient.post(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, userJson, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(response.statusCode(), 201);
	}
	
	
	@DataProvider
	public Object[][] getData() {
		
		return new Object[][] {
			{"karim", "male", "active"},
			{"anusha", "female", "inactive"},
			{"devi", "female", "inactive"}
		};
	}
	
	@DataProvider
	public Object[][] getExcelData() {
		
		return ExcelUtil.readData("GoRest");
	}
	
	@DataProvider
	public Object[][] getCSVData() {
		
		return CSVUtil.readData("CreateGoRestUsers"); //file name as argument
	}
	
	@Owner("Anil Kumar Cheepuru")
	@Severity(SeverityLevel.CRITICAL)
	@Description("Create a user with POST call with data from a CSV.")
	@Test(dataProvider = "getCSVData")
	public void createUserWithPojoLombok(String name, String gender, String status) {
		
		String emailId = StringUtil.getRandomEmail();
		User user = new User(null, name, emailId, gender, status); 
		
		Response response = restClient.post(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, user, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(response.statusCode(), 201);
	}
	
	
	@BeforeClass
	public void goRestTokenSetup() {
		ConfigManager.set("bearer_token", "10c0ba53681b541d08ba132593d8f32c28b45c81fd7717b42cc1f833144f01c2");
	}	
	
	@Test
	public void createUserWithPojoBuilder() {
		
		String emailId = StringUtil.getRandomEmail();
		User user = User.builder()
							.name("Bucky Barnes")
							.email(emailId)
							.gender("male")
							.status("inactive")
							.build();
		
		Response response = restClient.post(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, user, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		ChainTestListener.log("Logging with Listener => "+response.getBody().asString());
		ChainTestListener.log(((Integer)response.statusCode()).toString());
		Assert.assertEquals(response.statusCode(), 201);
	}
	

	@Test
	public void createUserWithBodyInJsonFile() throws IOException {
		
		String emailId = StringUtil.getRandomEmail();
		
		String rawJson = new String(
				Files.readAllBytes(
				Paths.get("src/test/resources/jsons/user.json")));
		
		//replace the desired value
		String updatedJson = rawJson.replace("{{emailId}}", emailId);
		
		Response response = restClient.post(BASE_URL_GOREST, GOREST_USERS_ENDPOINT, updatedJson, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(response.statusCode(), 201);
	}
}
