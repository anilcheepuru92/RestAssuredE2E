package com.qa.api.products.tests;

import org.testng.annotations.Test;

import com.qa.api.base.BaseTest;
import com.qa.api.constants.AuthType;
import com.qa.api.pojo.Product;
import com.qa.api.utils.ObjectMapperUtil;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class GetProductsWithPOJODeserializationTest extends BaseTest{
	
	@Test
	public void getAllProducts() {
		
		Response response = restClient.get(BASE_URL_FAKE_PRODUCT, FAKE_PRODUCTS_ENDPOINT, null, null, AuthType.NO_AUTH, ContentType.ANY);
		Product[] products = ObjectMapperUtil.deserialize(response, Product[].class);
		
		for(Product product: products) {
			System.out.println("ID =>"+ product.getId());
			System.out.println("Title =>"+ product.getTitle());
			System.out.println("Price =>"+ product.getPrice());
			System.out.println("Description =>"+ product.getDescription());
			System.out.println("Category =>"+ product.getCategory());
			System.out.println("Image =>"+ product.getImage());
			System.out.println("Rating-rate =>"+ product.getRating().getRate());
			System.out.println("Rating-count =>"+ product.getRating().getCount());
			System.out.println("------------------------------");
		}
	}

}
