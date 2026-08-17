package com.qa.api.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.api.exceptions.APIException;

import io.restassured.response.Response;

public class ObjectMapperUtil {
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	public static <T>T deserialize(Response response, Class<T> targetClass) {
		
		try {
			return objectMapper.readValue(response.getBody().asString(), targetClass);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new APIException("Deserialization failed for: "+targetClass.getName());
		}
	}

}
