package com.jalasoft.todoly.icons;

import api.APIManager;
import framework.Environment;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.LoggerManager;

public class IconUnauthorizedTest {
    private static final Environment environment = Environment.getInstance();
    private static final APIManager apiManager = APIManager.getInstance();
    private final int idValid20 = 20;
    private static final LoggerManager log = LoggerManager.getInstance();

    @BeforeClass
    public void setup() {
        apiManager.setCredentials(environment.getInvalidUserName(), environment.getInvalidPassword());
    }

    @Test
    public void getIconById20WithoutAuthorization() {
        log.info("Test case: Verify that the error code of the response is 102 when a request 'GET' an icon without authentication is sent");
        String iconByIdEndpoint = String.format(environment.getIconByIdEndpoint(), idValid20);
        Response response = apiManager.get(iconByIdEndpoint);

        System.out.println(response.jsonPath().get().toString());

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNotNull(response.jsonPath().getString("ErrorMessage"), "Error Message was returned");
        Assert.assertNotNull(response.jsonPath().getString("ErrorCode"), "Error Code was returned");
        Assert.assertEquals("Not Authenticated", response.jsonPath().getString("ErrorMessage"));
        Assert.assertEquals("102", response.jsonPath().getString("ErrorCode"));
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
