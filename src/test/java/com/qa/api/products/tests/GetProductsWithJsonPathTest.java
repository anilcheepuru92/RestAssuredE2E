package com.qa.api.products.tests;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.annotations.Test;

import com.qa.api.base.BaseTest;
import com.qa.api.constants.AuthType;
import com.qa.api.utils.JsonPathValidatorUtil;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class GetProductsWithJsonPathTest extends BaseTest{
	
	@Test
	public void getAllProducts() {
		
		Response response = restClient.get(BASE_URL_FAKE_PRODUCT, FAKE_PRODUCTS_ENDPOINT, null, null, AuthType.NO_AUTH, ContentType.ANY);
		List<Number> priceList = JsonPathValidatorUtil.readList(response, "$[?(@.price>50)].price");
		System.out.println("Prices above $50 => "+ priceList);
		
		List<Number> idList = JsonPathValidatorUtil.readList(response, "$[?(@.price>50)].id");
		System.out.println("Ids having the price above $50 => "+ idList);
		
		List<Number> rateList = JsonPathValidatorUtil.readList(response, "$[?(@.price>50)].rating.rate");
		System.out.println("Rates for products having the price above $50 => "+ rateList);
		
		List<Number> countList = JsonPathValidatorUtil.readList(response, "$[?(@.price>50)].rating.count");
		System.out.println("Counts for products having the price above $50 => "+ countList);
		
		List<Map<String, Object>> idTitleList = JsonPathValidatorUtil.readListOfMaps(response, "$.*['id', 'title']");
		System.err.println("********** ID, TITLE **********");
		idTitleList.forEach(m -> {
			System.out.println("ID:"+m.get("id"));
			System.out.println("Title:"+m.get("title"));
			System.out.println("----------------------");
		});
		
		List<Map<String, Object>> idTitleCategoryList = JsonPathValidatorUtil.readListOfMaps(response, "$.*['id', 'title', 'category']");
		System.err.println("********** ID, TITLE, CATEGORY **********");
		idTitleCategoryList.forEach(m -> {
			System.out.println("ID:"+m.get("id"));
			System.out.println("Title:"+m.get("title"));
			System.out.println("Category:"+m.get("category"));
			System.out.println("----------------------");
		});
		
		List<Number> jewleryIdList = JsonPathValidatorUtil.readList(response, "$[?(@.category=='jewelery')].id");
		System.out.println("IDs with Jewlery as category => "+ jewleryIdList);
		
		List<Map<String, Object>> jewleryIdTitleList = JsonPathValidatorUtil.readListOfMaps(response, "$[?(@.category=='jewelery')].['id', 'title']");
		System.err.println("********** JEWLERY **********");
		jewleryIdTitleList.forEach(m -> {
			System.out.println("ID:"+m.get("id"));
			System.out.println("Title:"+m.get("title"));
			System.out.println("----------------------");
		});
		
		Double minPrice = JsonPathValidatorUtil.read(response, "min($[*].price)");
		System.out.println("Minimum Price => "+ minPrice);
	}

}
