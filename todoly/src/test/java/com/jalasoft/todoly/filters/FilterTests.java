package com.jalasoft.todoly.filters;

import api.APIManager;
import framework.Environment;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.LoggerManager;

public class FilterTests {
    private static final Environment environment = Environment.getInstance();
    private static final APIManager apiManager = APIManager.getInstance();
    private static final LoggerManager log = LoggerManager.getInstance();

    @BeforeClass
    public void setup() {
        apiManager.setCredentials(environment.getUserName(), environment.getPassword());
    }
    @Test
    public void getAllFilters() {
        log.info("Verify that is possible to obtain a list of all Filters of an Authenticated user when doing GET request to \"https://todo.ly/api/filters.json\"");
        Response response = apiManager.get(environment.getFilterEndpoint());

        System.out.println(response.jsonPath().get().toString());

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertFalse(response.getBody().asString().contains("ErrorMessage"), "Correct response body is returned");
        Assert.assertFalse(response.getBody().asString().contains("ErrorCode"), "Correct response body is not returned");
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
