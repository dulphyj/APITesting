package com.jalasoft.todoly.filters;

import api.APIManager;
import constants.Constants;
import entities.filters.Filter;
import framework.Environment;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.LoggerManager;

public class FilterByIdTests {
    private static final Environment environment = Environment.getInstance();
    private static final APIManager apiManager = APIManager.getInstance();

    private static final LoggerManager log = LoggerManager.getInstance();


    @BeforeClass
    public void setup() {
        apiManager.setCredentials(environment.getUserName(), environment.getPassword());
    }

    @Test
    public void getFilterByIdFirstFilter() {

        String filterByIdEndpoint = String.format(environment.getFilterByIdEndpoint(), Constants.VALIDFILTERIDFIRST);
        Response response = apiManager.get(filterByIdEndpoint);
        Filter responseFilter = response.as(Filter.class);

        log.info("Verify that is possible to obtain a list of the filter with the given Id when doing a GET request \"https://todo.ly/api/filters/-1.json\"");

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNull(response.jsonPath().getString("ErrorMessage"), "Error Message was returned");
        Assert.assertNull(response.jsonPath().getString("ErrorCode"), "Error Code was returned");
        Assert.assertEquals(responseFilter.getId(), -1, "Id value is incorrect");
        Assert.assertEquals(responseFilter.getContent(), "Today", "Content value is incorrect");
        Assert.assertEquals(responseFilter.getIcon(), 16, "Icon value is incorrect");
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
