package com.jalasoft.todoly.icons;

import api.APIManager;
import constants.Constants;
import entities.icons.Icon;
import framework.Environment;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.LoggerManager;

public class IconsTests {
    private static final Environment environment = Environment.getInstance();
    private static final APIManager apiManager = APIManager.getInstance();
    private static final LoggerManager log = LoggerManager.getInstance();

    @BeforeClass
    public void setup() {
        apiManager.setCredentials(environment.getUserName(), environment.getPassword());
    }

    @Test
    public void getIconById0() {
        log.info("Test case: Verify that the response is correct when a request 'GET' a valid icon id = 0 is sent");
        String iconByIdEndpoint = String.format(environment.getIconByIdEndpoint(), Constants.VALIDID0);
        Response response = apiManager.get(iconByIdEndpoint);
        Icon responseIcon = response.as(Icon.class);

        System.out.println(response.jsonPath().get().toString());

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNull(response.jsonPath().getString("ErrorMessage"), "Error Message was returned");
        Assert.assertNull(response.jsonPath().getString("ErrorCode"), "Error Code was returned");
        Assert.assertEquals(responseIcon.getId(), 0, "Id value is incorrect");
        Assert.assertEquals(responseIcon.getUrl(), "http://todo.ly/Images/icons/page2.png", "Content value is incorrect");
    }

    @Test
    public void getIconById20() {
        log.info("Test case: Verify that the response is correct when a request 'GET' a valid icon id = 20 is sent");
        String iconByIdEndpoint = String.format(environment.getIconByIdEndpoint(), Constants.VALIDID20);
        Response response = apiManager.get(iconByIdEndpoint);
        Icon responseIcon = response.as(Icon.class);

        System.out.println(response.jsonPath().get().toString());

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNull(response.jsonPath().getString("ErrorMessage"), "Error Message was returned");
        Assert.assertNull(response.jsonPath().getString("ErrorCode"), "Error Code was returned");
        Assert.assertEquals(responseIcon.getId(), 20, "Id value is incorrect");
        Assert.assertEquals(responseIcon.getUrl(), "http://todo.ly/Images/icons/next.png", "Content value is incorrect");
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
