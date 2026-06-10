package com.qa.api.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class LoginApiTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://reqres.in";
    }

    @Test
    public void verifySuccessfulLogin() {

        Map<String, String> requestBody = new HashMap<>();

        requestBody.put("email", "eve.holt@reqres.in");
        requestBody.put("password", "cityslicka");

        Response response =
                RestAssured
                        .given()
                        .contentType(ContentType.JSON)
                        .body(requestBody)
                        .when()
                        .post("/api/login")
                        .then()
                        .extract()
                        .response();

        System.out.println("Response Body:");
        System.out.println(response.getBody().asString());

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200,
                "Status code validation failed");

        String token = response.jsonPath().getString("token");

        Assert.assertNotNull(token,
                "Token should not be null");

        Assert.assertFalse(token.isEmpty(),
                "Token should not be empty");

        System.out.println("Generated Token: " + token);
    }

    @Test
    public void verifyLoginWithoutPassword() {

        Map<String, String> requestBody = new HashMap<>();

        requestBody.put("email", "peter@klaven");

        Response response =
                RestAssured
                        .given()
                        .contentType(ContentType.JSON)
                        .body(requestBody)
                        .when()
                        .post("/api/login")
                        .then()
                        .extract()
                        .response();

        System.out.println(response.getBody().asString());

        Assert.assertEquals(response.getStatusCode(), 400);

        String errorMessage =
                response.jsonPath().getString("error");

        Assert.assertEquals(
                errorMessage,
                "Missing password"
        );
    }

    @Test
    public void verifyResponseTime() {

        Map<String, String> requestBody = new HashMap<>();

        requestBody.put("email", "eve.holt@reqres.in");
        requestBody.put("password", "cityslicka");

        Response response =
                RestAssured
                        .given()
                        .contentType(ContentType.JSON)
                        .body(requestBody)
                        .when()
                        .post("/api/login");

        long responseTime =
                response.getTime();

        System.out.println(
                "Response Time: "
                        + responseTime + " ms");

        Assert.assertTrue(
                responseTime < 3000,
                "Response time exceeded limit"
        );
    }
}
