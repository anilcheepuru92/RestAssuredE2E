package com.qa.api.products.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.api.base.BaseTest;
import com.qa.api.constants.AuthType;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class GetProductsTest extends BaseTest{
	
	@Test
	public void getAllProducts() {
		
		Response response = restClient.get(BASE_URL_FAKE_PRODUCT, FAKE_PRODUCTS_ENDPOINT, null, null, AuthType.NO_AUTH, ContentType.JSON);
		Assert.assertEquals(response.statusCode(), 200);
		Assert.assertTrue(response.statusLine().contains("OK"));
	}

}
