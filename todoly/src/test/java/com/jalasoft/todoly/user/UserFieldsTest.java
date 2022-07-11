package com.jalasoft.todoly.user;

import api.APIManager;
import api.methods.APIUserMethods;
import constants.Constants;
import entities.user.NewUser;
import framework.Environment;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import utils.LoggerManager;

public class UserFieldsTest {
    private static final Environment environment = Environment.getInstance();
    private static final APIManager apiManager = APIManager.getInstance();
    private static final LoggerManager log = LoggerManager.getInstance();

    @Test
    public void sameEmailAndPassword(){
        log.info("Verify that is not possible create a new user account setting the same email and password when doing a POST request to \"https://todo.ly/api/user.json\"");
        NewUser newUser = new NewUser(Constants.EMAIL_NT,Constants.FULLNAME_NT, Constants.EMAIL_NT);
        Response response = apiManager.post(environment.getUserEndpoint(), ContentType.JSON, newUser);

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertEquals("Invalid Password", response.jsonPath().getString("ErrorMessage"), "Error Message was not returned");
        Assert.assertEquals("310", response.jsonPath().getString("ErrorCode"), "Error Code was not returned");
    }

    @AfterMethod
    public void teardownSameEmailAndPassword() {
        boolean isUserDeleted = APIUserMethods.deleteUser(Constants.EMAIL_NT,Constants.EMAIL_NT);
        Assert.assertTrue(isUserDeleted, "User was not deleted");
    }

    @Test
    public void createUserInvalidFullNameWhiteSpaces(){
        log.info("Verify that is not possible create a new user account filling the Full Name field with only white spaces when doing a POST request to \"https://todo.ly/api/user.json\"");
        NewUser newUser = new NewUser(Constants.EMAIL_NT,Constants.FULLNAMEEMPTY, Constants.PASSWORD_NT);
        Response response = apiManager.post(environment.getUserEndpoint(), ContentType.JSON, newUser);

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertEquals("Invalid FullName", response.jsonPath().getString("ErrorMessage"), "Error Message was not returned");
        Assert.assertEquals("306", response.jsonPath().getString("ErrorCode"), "Error Code was not returned");
    }

    @AfterMethod
    public void teardownCreateUserInvalidFullNameWhiteSpaces() {
        boolean isUserDeleted = APIUserMethods.deleteUser(Constants.EMAIL_NT,Constants.PASSWORD_NT);
        Assert.assertTrue(isUserDeleted, "User was not deleted");
    }

    @Test
    public void createUserInvalidFullNameSpecialCharacters(){
        log.info("Verify that is not possible create a new user account filling the Full Name field with only special characters when doing a POST request to \"https://todo.ly/api/user.json\"");
        NewUser newUser = new NewUser(Constants.EMAIL_NT,Constants.FULLNAMESPECIALCHARACTERS, Constants.PASSWORD_NT);
        Response response = apiManager.post(environment.getUserEndpoint(), ContentType.JSON, newUser);

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertEquals("Invalid FullName", response.jsonPath().getString("ErrorMessage"), "Error Message was not returned");
        Assert.assertEquals("306", response.jsonPath().getString("ErrorCode"), "Error Code was not returned");
    }

    @AfterMethod
    public void teardownCreateUserInvalidFullNameSpecialCharacters() {
        boolean isUserDeleted = APIUserMethods.deleteUser(Constants.EMAIL_NT,Constants.PASSWORD_NT);
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
