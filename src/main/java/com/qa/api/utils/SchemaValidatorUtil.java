package com.qa.api.utils;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import io.restassured.response.Response;

public class SchemaValidatorUtil {

	public static boolean validateSchema(Response response, String schemaFileName) {
		
		try {
			response.then()
						.assertThat()
							.body(matchesJsonSchemaInClasspath(schemaFileName));
			
			System.out.println("Schema validation is successful for: "+schemaFileName);
			return true;
		} catch (Exception e) {
			System.out.println("Schema validation is failed for: "+schemaFileName);
			return false;
		}
	}
}
