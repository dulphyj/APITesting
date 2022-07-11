package com.jalasoft.todoly.authentication;

import api.APIManager;
import api.methods.APITokenMethods;
import entities.token.Token;
import framework.Environment;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.LoggerManager;
import java.util.ArrayList;
import java.util.Objects;

public class tokenTests {
    private static final Environment environment = Environment.getInstance();
    private static final APIManager apiManager = APIManager.getInstance();
    private final ArrayList<Token> tokens = new ArrayList<>();
    private static final LoggerManager log = LoggerManager.getInstance();

    @BeforeClass
    public void setup() {
        apiManager.setCredentials(environment.getUserName(), environment.getPassword());
    }

    @Test
    public void getToken() {
        log.info("Test case: Verify that is able to get a token with authentication when a request 'GET' is sent");
        String tokenEndpoint = String.format(environment.getTokenEndpoint());
        Response response = apiManager.get(tokenEndpoint);
        Token responseToken = response.as(Token.class);
        tokens.add(responseToken);

        System.out.println(response.jsonPath().get().toString());

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNull(response.jsonPath().getString("ErrorMessage"), "Error Message was returned");
        Assert.assertNull(response.jsonPath().getString("ErrorCode"), "Error Code was returned");
        Assert.assertEquals(responseToken.getUserEmail(), environment.getUserName(), "UserEmail value is incorrect");
    }

    @Test
    public void deleteToken() {
        log.info("Test case: Verify that is able to delete a valid token with basic authentication when a request 'DELETE' is sent");
        String tokenEndpoint = String.format(environment.getTokenEndpoint());
        Response response = apiManager.get(tokenEndpoint);
        Token responseCreateToken = response.as(Token.class);

        Response responseDel = apiManager.deleteWithToken(tokenEndpoint, "Token", responseCreateToken.getTokenString());
        Token responseDeleteToken = responseDel.as(Token.class);

        System.out.println(response.jsonPath().get().toString());

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNull(response.jsonPath().getString("ErrorMessage"), "Error Message was returned");
        Assert.assertNull(response.jsonPath().getString("ErrorCode"), "Error Code was returned");
        Assert.assertEquals(responseCreateToken.getUserEmail(), responseDeleteToken.getUserEmail(), "UserEmail value is incorrect");
        Assert.assertEquals(responseCreateToken.getTokenString(), responseDeleteToken.getTokenString(), "TokenString value is incorrect");
        Assert.assertEquals(responseCreateToken.getExpirationTime(), responseDeleteToken.getExpirationTime(), "ExpirationTime value is incorrect");
    }

    @AfterClass
    public void teardown() {
        tokens.removeIf(Objects::isNull);
        if (tokens.size() > 0) {
            for (Token token : tokens) {
                boolean isTokenDeleted = APITokenMethods.deleteToken(token.getTokenString());
                Assert.assertTrue(isTokenDeleted, "Token was not deleted");
            }
        }
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
