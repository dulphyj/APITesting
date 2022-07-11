package com.jalasoft.todoly.authentication;

import api.APIManager;
import entities.token.Token;
import framework.Environment;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.LoggerManager;

import java.util.ArrayList;

public class tokenNegativeTests {
    private static final Environment environment = Environment.getInstance();
    private static final APIManager apiManager = APIManager.getInstance();
    private final String invalidToken = "invalid token";
    private final ArrayList<Token> tokens = new ArrayList<>();
    private static final LoggerManager log = LoggerManager.getInstance();

    @BeforeMethod
    public void setup() {
        apiManager.setCredentials(environment.getUserName(), environment.getPassword());
    }

    @Test
    public void deleteAnInvalidToken() {
        log.info("Test case: Verify that the error code of the response is 103 when a request 'DELETE' an invalid token with basic auth is sent");
        String isAuthEndpoint = String.format(environment.getTokenEndpoint());
        Response response = apiManager.deleteWithToken(isAuthEndpoint, "Token", invalidToken);

        System.out.println(response.body().asString());

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNotNull(response.jsonPath().getString("ErrorMessage"), "Error Message was returned");
        Assert.assertNotNull(response.jsonPath().getString("ErrorCode"), "Error Code was returned");
        Assert.assertEquals("Invalid Token", response.jsonPath().getString("ErrorMessage"), "Error Message was returned");
        Assert.assertEquals("103", response.jsonPath().getString("ErrorCode"), "Error Message was returned");
    }

    @Test
    public void deleteAValidTokenWithoutAuthentication() {
        log.info("Test case: Verify that the error code of the response is 102 when a request 'DELETE' a valid token without basic auth is sent");
        String tokenEndpoint = String.format(environment.getTokenEndpoint());
        Response response = apiManager.get(tokenEndpoint);
        Token responseCreateToken = response.as(Token.class);
        tokens.add(responseCreateToken);
        apiManager.setCredentials("", "");
        String isAuthEndpoint = String.format(environment.getTokenEndpoint());
        Response responseDelete = apiManager.deleteWithToken(isAuthEndpoint, "Token", responseCreateToken.getTokenString());

        System.out.println(responseDelete.body().asString());

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNotNull(response.jsonPath().getString("ErrorMessage"), "Error Message was returned");
        Assert.assertNotNull(response.jsonPath().getString("ErrorCode"), "Error Code was returned");
        Assert.assertEquals("Not Authenticated", responseDelete.jsonPath().getString("ErrorMessage"), "Error Message was returned");
        Assert.assertEquals("102", responseDelete.jsonPath().getString("ErrorCode"), "Error Message was returned");
    }
    @AfterMethod
    public void afterMethod(ITestResult result) {
        try {
            if(result.getStatus() == ITestResult.SUCCESS) {
                log.warn("Test 'PASSED'");
            }
            else if(result.getStatus() == ITestResult.FAILURE) {
                log.warn("Test 'FAILED'");
            }
            else if(result.getStatus() == ITestResult.SKIP ) {
                log.warn("Test 'BLOCKED'");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
