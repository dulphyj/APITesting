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

public class IsAuthenticatedTests {
    private static final Environment environment = Environment.getInstance();
    private static final APIManager apiManager = APIManager.getInstance();
    private final ArrayList<Token> tokens = new ArrayList<>();
    private String token;
    private static final LoggerManager log = LoggerManager.getInstance();

    @Test
    public void isAuthenticatedWithBasicAuth() {
        log.info("Test case: Verify that the response of IsAuthenticated with authorization is 'true'");
        apiManager.setCredentials(environment.getUserName(), environment.getPassword());
        String isAuthEndpoint = String.format(environment.isAuthenticatedEndpoint());
        Response response = apiManager.get(isAuthEndpoint);

        System.out.println(response.body().asString());

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertTrue(response.body().asString().contains("true"), "Correct response body is returned");
    }

    @BeforeClass
    public void setup() {
        apiManager.setCredentials(environment.getUserName(), environment.getPassword());
        String tokenEndpoint = String.format(environment.getTokenEndpoint());
        Response response = apiManager.get(tokenEndpoint);
        Token responseCreateToken = response.as(Token.class);
        tokens.add(responseCreateToken);
        token = responseCreateToken.getTokenString();
    }

    @Test
    public void isAuthenticatedWithToken() {
        log.info("Test case: Verify that can have authentication with a valid token when a request 'GET' is sent");
        apiManager.setCredentials("", "");
        String isAuthEndpoint = String.format(environment.isAuthenticatedEndpoint());
        Response response = apiManager.getWithToken(isAuthEndpoint, "Token", token);

        System.out.println(response.body().asString());

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertTrue(response.body().asString().contains("true"), "Correct response body is returned");
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
