package com.jalasoft.todoly.filters;

import api.APIManager;
import constants.Constants;
import entities.filters.Filter;
import framework.Environment;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import utils.LoggerManager;

public class FilterNegativeTests {

    private static final Environment environment = Environment.getInstance();
    private static final APIManager apiManager = APIManager.getInstance();
    private static final LoggerManager log = LoggerManager.getInstance();

    @Test
    public void getAllFiltersWithoutAuthentication() {
        log.info("Verify that cannot get a list of all Filters without authentication when doing a GET request \"https://todo.ly/api/filters.json\"");
        Response response = apiManager.get(environment.getFilterEndpoint());

        System.out.println(response.jsonPath().get().toString());

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertTrue(response.getBody().asString().contains("ErrorMessage"), "Correct response body is returned");
        Assert.assertTrue(response.getBody().asString().contains("ErrorCode"), "Correct response body is not returned");
    }

    @Test
    public void getFilterByIdFirstFilterWithoutAuthentication() {

        String filterByIdEndpoint = String.format(environment.getFilterByIdEndpoint(), Constants.VALIDFILTERIDFIRST);
        Response response = apiManager.get(filterByIdEndpoint);

        log.info("Verify that cannot get a list of Filters with given Id without authentication when doing a GET request \"https://todo.ly/api/filters/-1.json\"");

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertTrue(response.getBody().asString().contains("ErrorMessage"), "Correct response body is returned");
        Assert.assertTrue(response.getBody().asString().contains("ErrorCode"), "Correct response body is not returned");
    }

    @Test
    public void getItemsByIdGivenFilterWithoutAuthentication() {

        String filterByIdEndpoint = String.format(environment.getItemsOfAFilter());
        Response response = apiManager.get(filterByIdEndpoint);

        log.info("Verify that is possible to obtain a list of Items within the given filter when doing a GET request \"https://todo.ly/api/filters/-1/items.json\"");

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertTrue(response.getBody().asString().contains("ErrorMessage"), "Correct response body is returned");
        Assert.assertTrue(response.getBody().asString().contains("ErrorCode"), "Correct response body is not returned");
    }

    @Test
    public void getDoneItemsByIdGivenFilterWithoutAuthentication() {

        String filterByIdEndpoint = String.format(environment.getDoneItemsEndPoint());
        Response response = apiManager.get(filterByIdEndpoint);

        log.info("Verify that cannot get a list of Done Items with given the filter without authentication when doing a GET request \"https://todo.ly/api/filters/-1/doneitems.json\"");

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertTrue(response.getBody().asString().contains("ErrorMessage"), "Correct response body is returned");
        Assert.assertTrue(response.getBody().asString().contains("ErrorCode"), "Correct response body is not returned");
    }

    @Test
    public  void getInvalidResponseIntroducingNonExistentItemIdOfInboxFilter() {
        apiManager.setCredentials(environment.getUserName(), environment.getPassword());
        String filterByIdEndpoint = String.format(environment.getItemsOfInboxFilterByIdEndpoint(),123123123);
        Response response = apiManager.get(filterByIdEndpoint);

        log.info("Verify that is possible to obtain a list of Items within the given filter when doing a GET request \"https://todo.ly/api/filters/-1/items.json\"");

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertTrue(response.getBody().asString().contains("ErrorMessage"), "Correct response body is returned");
        Assert.assertTrue(response.getBody().asString().contains("ErrorCode"), "Correct response body is not returned");
    }
    @Test
    public  void getInvalidResponseIntroducingNonExistentItemIdOfTodayFilter() {
        apiManager.setCredentials(environment.getUserName(), environment.getPassword());
        String filterByIdEndpoint = String.format(environment.getItemsOfTodayFilterByIdEndpoint(),213123);
        Response response = apiManager.get(filterByIdEndpoint);

        log.info("Verify that is possible to obtain a list of Items within the given filter when doing a GET request \"https://todo.ly/api/filters/-1/items.json\"");

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertTrue(response.getBody().asString().contains("ErrorMessage"), "Correct response body is returned");
        Assert.assertTrue(response.getBody().asString().contains("ErrorCode"), "Correct response body is not returned");
    }
    @Test
    public  void getInvalidResponseIntroducingNonExistentItemIdOfNextFilter() {
        apiManager.setCredentials(environment.getUserName(), environment.getPassword());
        String filterByIdEndpoint = String.format(environment.getItemsOfNextFilterByIdEndpoint(),2143214);
        Response response = apiManager.get(filterByIdEndpoint);

        log.info("Verify that is possible to obtain a list of Items within the given filter when doing a GET request \"https://todo.ly/api/filters/-1/items.json\"");

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertTrue(response.getBody().asString().contains("ErrorMessage"), "Correct response body is returned");
        Assert.assertTrue(response.getBody().asString().contains("ErrorCode"), "Correct response body is not returned");
    }

    @Test
    public void getFilterByIdLastFilter() {

        apiManager.setCredentials(environment.getUserName(), environment.getPassword());
        String filterByIdEndpoint = String.format(environment.getFilterByIdEndpoint(), Constants.VALIDFILTERIDLAST);
        Response response = apiManager.get(filterByIdEndpoint);
        Filter responseFilter = response.as(Filter.class);

        log.info("Verify that is possible to obtain data of the last filter \"Recycle Bin\" with the give Id when doing a GET request \"https://todo.ly/api/filters/-3.json\"");

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNull(response.jsonPath().getString("ErrorMessage"), "Error Message was returned");
        Assert.assertNull(response.jsonPath().getString("ErrorCode"), "Error Code was returned");
        Assert.assertEquals(responseFilter.getId(), -3, "Id value is incorrect");
        Assert.assertEquals(responseFilter.getContent(), "Recycle Bin", "Content value is incorrect");
        Assert.assertEquals(responseFilter.getIcon(), 18, "Icon value is incorrect");
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
