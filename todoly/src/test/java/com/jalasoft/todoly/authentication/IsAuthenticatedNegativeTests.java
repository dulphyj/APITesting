package com.jalasoft.todoly.authentication;

import api.APIManager;
import framework.Environment;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import utils.LoggerManager;

public class IsAuthenticatedNegativeTests {
    private static final Environment environment = Environment.getInstance();
    private static final APIManager apiManager = APIManager.getInstance();
    private final String invalidToken = "invalid token";
    private static final LoggerManager log = LoggerManager.getInstance();

    @Test
    public void isNotAuthenticatedWithBasicAuth() {
        log.info("Test case: Verify that the response of IsAuthenticated without authorization is 'false'");
        String isAuthEndpoint = String.format(environment.isAuthenticatedEndpoint());
        Response response = apiManager.get(isAuthEndpoint);

        System.out.println(response.body().asString());

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertTrue(response.body().asString().contains("false"), "Correct response body is returned");
    }

    @Test
    public void isNotAuthenticatedWithInvalidToken() {
        log.info("Test case: Verify that the response is 'false' when a request 'GET' authentication with an invalid token is sent");
        String isAuthEndpoint = String.format(environment.isAuthenticatedEndpoint());
        Response response = apiManager.getWithToken(isAuthEndpoint, "Token", invalidToken);

        System.out.println(response.body().asString());

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertTrue(response.body().asString().contains("false"), "Correct response body is returned");
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
