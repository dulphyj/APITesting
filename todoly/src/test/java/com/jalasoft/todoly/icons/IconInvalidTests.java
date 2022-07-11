package com.jalasoft.todoly.icons;

import api.APIManager;
import constants.Constants;
import framework.Environment;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.LoggerManager;

public class IconInvalidTests {
    private static final Environment environment = Environment.getInstance();
    private static final APIManager apiManager = APIManager.getInstance();
    private static final LoggerManager log = LoggerManager.getInstance();

    @BeforeClass
    public void setup() {
        apiManager.setCredentials(environment.getUserName(), environment.getPassword());
    }

    @Test
    public void getIconByInvalidId21() {
        log.info("Test case: Verify that the error code of the response is 301 when a request 'GET' an invalid icon id = 21 is sent");
        String iconByIdEndpoint = String.format(environment.getIconByIdEndpoint(), Constants.INVALIDID21);
        Response response = apiManager.get(iconByIdEndpoint);

        System.out.println(response.jsonPath().get().toString());

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNotNull(response.jsonPath().getString("ErrorMessage"), "Error Message was returned");
        Assert.assertNotNull(response.jsonPath().getString("ErrorCode"), "Error Code was returned");
        Assert.assertEquals("Invalid Id", response.jsonPath().getString("ErrorMessage"), "Error Message was not returned");
        Assert.assertEquals("301", response.jsonPath().getString("ErrorCode"), "Error Code was not returned");
    }
    @DataProvider(name = "data-provider")
    public Object[][] getData() {
        return new Object[][]
                {
                        {"-1"},
                        {"one"},
                        {""}
                };
    }
    @Test(dataProvider = "data-provider")
    public void getIconByInvalidDataProvider(String iconId) {
        log.info("Test case: Verify that the error code of the response is 101 when a request 'GET' an invalid icon id = "+ iconId +"is sent");
        System.out.println("icon id is: " + iconId);
        String iconByIdEndpoint = environment.getIconByIdEndpoint().replace("%d", iconId);
        Response response = apiManager.get(iconByIdEndpoint);

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNotNull(response.jsonPath().getString("ErrorMessage"), "Error Message was returned");
        Assert.assertNotNull(response.jsonPath().getString("ErrorCode"), "Error Code was returned");
        Assert.assertEquals("Invalid Request", response.jsonPath().getString("ErrorMessage"), "Error Message was not returned");
        Assert.assertEquals("101", response.jsonPath().getString("ErrorCode"), "Error Code was not returned");
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
