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
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.LoggerManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserErrorCodeTests {

    private static final Environment environment = Environment.getInstance();
    private static final APIManager apiManager = APIManager.getInstance();
    private static final LoggerManager log = LoggerManager.getInstance();
    private final ArrayList<User> users = new ArrayList<>();
    Map<String, Object> jsonAsMap = new HashMap<>();

    @BeforeClass
    public void setup() {
        users.add(APIUserMethods.createUser(Constants.EMAIL1_EC, Constants.USERNAME_EC, Constants.PASSWORD_EC));
    }
    @Test
    public void userAccountAlreadyExist(){
        log.info("Verify that is not possible create a new user account with an email that already exist when doing a POST request to \"https://todo.ly/api/user.json\"");
        NewUser newUser = new NewUser(Constants.EMAIL1_EC,Constants.USERNAME_EC, Constants.PASSWORD_EC);
        Response response = apiManager.post(environment.getUserEndpoint(), ContentType.JSON, newUser);

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertEquals("Account with this email address already exists", response.jsonPath().getString("ErrorMessage"), "Error Message was not returned");
        Assert.assertEquals("201", response.jsonPath().getString("ErrorCode"), "Error Code was not returned");
    }

    @Test
    public void passwordTooShort(){
        log.info("Verify that is not possible create a new user account with empty password when doing a POST request to \"https://todo.ly/api/user.json\"");
        NewUser newUser = new NewUser(Constants.EMAIL2_EC,Constants.USERNAME_EC, "");
        Response response = apiManager.post(environment.getUserEndpoint(), ContentType.JSON, newUser);

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertEquals("Password too short", response.jsonPath().getString("ErrorMessage"), "Error Message was not returned");
        Assert.assertEquals("202", response.jsonPath().getString("ErrorCode"), "Error Code was not returned");
    }

    @Test
    public void invalidEmailAddress(){
        log.info("Verify that is not possible create a new user account with an invalid email when doing a POST request to \"https://todo.ly/api/user.json\"");
        NewUser newUser = new NewUser(Constants.INVALIDEMAIL_EC,Constants.USERNAME_EC, Constants.PASSWORD_EC);
        Response response = apiManager.post(environment.getUserEndpoint(), ContentType.JSON, newUser);

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertEquals("Invalid Email Address", response.jsonPath().getString("ErrorMessage"), "Error Message was not returned");
        Assert.assertEquals("307", response.jsonPath().getString("ErrorCode"), "Error Code was not returned");
    }

    @Test
    public void invalidFullName(){
        log.info("Verify that is not possible create a new user account with empty full name when doing a POST request to \"https://todo.ly/api/user.json\"");
        NewUser newUser = new NewUser(Constants.EMAIL2_EC,"", Constants.PASSWORD_EC);
        Response response = apiManager.post(environment.getUserEndpoint(), ContentType.JSON, newUser);

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertEquals("Invalid FullName", response.jsonPath().getString("ErrorMessage"), "Error Message was not returned");
        Assert.assertEquals("306", response.jsonPath().getString("ErrorCode"), "Error Code was not returned");
    }

    @Test
    public void emptyEmail(){
        log.info("Verify that is not possible create a new user account with empty email when doing a POST request to \"https://todo.ly/api/user.json\"");
        NewUser newUser = new NewUser("",Constants.USERNAME_EC,Constants.PASSWORD_EC);
        Response response = apiManager.post(environment.getUserEndpoint(), ContentType.JSON, newUser);

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertEquals("Invalid Email Address", response.jsonPath().getString("ErrorMessage"), "Error Message was not returned");
        Assert.assertEquals("307", response.jsonPath().getString("ErrorCode"), "Error Code was not returned");
    }

    @Test
    public void updateWithInvalidEmail(){
        log.info("Verify that is not possible update a user account with an invalid email when doing a UPDATE request to \"https://todo.ly/api/user/0.json\"");
        apiManager.setCredentials(Constants.EMAIL1_EC,Constants.PASSWORD_EC);
        jsonAsMap.put("Email", "testtesttes.com");

        Response response = apiManager.put(environment.getUserUpdateEndpoint(), ContentType.JSON, jsonAsMap );

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertEquals("Invalid input Data", response.jsonPath().getString("ErrorMessage"), "Error Message was not returned");
        Assert.assertEquals("302", response.jsonPath().getString("ErrorCode"), "Error Code was not returned");
    }

    @AfterClass
    public void teardown() {
        boolean isUserDeleted = APIUserMethods.deleteUser(Constants.EMAIL1_EC,Constants.PASSWORD_EC);
        Assert.assertTrue(isUserDeleted, "User was not deleted");
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
