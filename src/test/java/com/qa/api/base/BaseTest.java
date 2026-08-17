package com.qa.api.base;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;

import com.aventstack.chaintest.plugins.ChainTestListener;
import com.qa.api.client.RestClient;
import com.qa.api.manager.ConfigManager;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;

@Listeners(ChainTestListener.class)
public class BaseTest {
	
	protected static String BASE_URL_GOREST;
	
	//******************* API Base URLs *******************//
//	protected final static String BASE_URL_GOREST = "https://gorest.co.in";
	protected final static String BASE_URL_FAKE_PRODUCT = "https://fakestoreapi.com";
	protected final static String BASE_URL_HEROKU = "https://the-internet.herokuapp.com";
	protected final static String BASE_URL_SPOTIFY_TOKEN = "https://accounts.spotify.com";
	protected final static String BASE_URL_SPOTIFY_ALBUMS = "https://api.spotify.com";
	protected final static String BASE_URL_GOOGLE_GEMINI = "https://generativelanguage.googleapis.com";
	
	
	//******************* API End Points *******************//
	protected final static String GOREST_USERS_ENDPOINT = "/public/v2/users";
	protected final static String FAKE_PRODUCTS_ENDPOINT = "/products";
	protected final static String HEROKU_BASICAUTH_ENDPOINT = "/basic_auth";
	protected final static String SPOTIFY_TOKEN_ENDPOINT = "/api/token";
	protected final static String SPOTIFY_ALBUMS_ENDPOINT = "/v1/albums/";
	protected final static String GOOGLE_GEMINI_ENDPOINT = "/v1beta/models/gemini-flash-latest:generateContent";
	
	protected RestClient restClient;
	
	@BeforeSuite
	public void allureSetup() {
		
		RestAssured.filters(new AllureRestAssured());
		BASE_URL_GOREST = ConfigManager.get("baseurl_gorest");
	}
	
	
	@BeforeTest
	public void initSetup() {
		
		restClient = new RestClient();
	}

	@AfterTest
	public void teardown() {
		
		
	}
	
}
