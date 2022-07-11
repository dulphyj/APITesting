package com.jalasoft.todoly.user;

import api.APIManager;
import api.methods.APIUserMethods;
import constants.Constants;
import entities.user.NewUser;
import entities.user.User;
import framework.Environment;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.LoggerManager;

public class UserTests {
    private static final Environment environment = Environment.getInstance();
    private static final APIManager apiManager = APIManager.getInstance();

    private static final LoggerManager log = LoggerManager.getInstance();
    @BeforeClass
    public void setup() {
        apiManager.setCredentials(environment.getUserName(), environment.getPassword());
    }

    @Test
    public void getUserInfo() {
        log.info("Verify that is possible return information data of a current authenticated user when doing a GET request to  \"/https://todo.ly/api/user.json\"");
        apiManager.setCredentials(environment.getUserName(), environment.getPassword());
        Response response = apiManager.get(environment.getUserEndpoint());

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertFalse(response.getBody().asString().contains("ErrorMessage"), "Correct response body is returned");
        Assert.assertFalse(response.getBody().asString().contains("ErrorCode"), "Correct response body is not returned");
    }

    @Test
    public void createNewUser() {
        log.info("Verify that is possible create a new user account with valid information when doing a POST request to \"/https://todo.ly/api/user.json\"");
        NewUser newUser = new NewUser(Constants.USERNAMETESTCRUD, Constants.FULLNAMETESTCRUD, Constants.PASSWORDTESTCRUD);
        Response response = apiManager.post(environment.getUserEndpoint(), ContentType.JSON, newUser);
        User responseUser = response.as(User.class);

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNull(response.jsonPath().getString("ErrorMessage"), "Error Message was returned");
        Assert.assertNull(response.jsonPath().getString("ErrorCode"), "Error Code was returned");
        Assert.assertEquals(responseUser.getEmail(), newUser.getEmail(), "Incorrect Email value was set");
        Assert.assertEquals(responseUser.getFullName(), newUser.getFullName(), "Incorrect Full Name value was set");
    }

    @AfterClass
    public void teardown() {
        boolean isUserDeleted = APIUserMethods.deleteUser(Constants.USERNAMETESTCRUD,Constants.PASSWORDTESTCRUD);
        Assert.assertTrue(isUserDeleted, "User was not deleted");
    }

    @Test
    public void deleteUser() {
        log.info("Verify that is possible delete user account with valid information when doing a DELETE request to \"https://todo.ly/api/user.json\"");
        NewUser newUser = new NewUser("hola@hola.com", "holaaaaa","hola1230");
        Response response = apiManager.post(environment.getUserEndpoint(), ContentType.JSON, newUser);
        apiManager.setCredentials("hola@hola.com","hola1230");
        apiManager.delete(environment.getUserDeleteEndpoint());

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