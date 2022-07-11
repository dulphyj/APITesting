package com.jalasoft.todoly.user;

import api.APIManager;
import constants.Constants;
import framework.Environment;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.LoggerManager;
import java.util.HashMap;
import java.util.Map;

public class UserNegativeTests {
    private static final Environment environment = Environment.getInstance();
    private static final APIManager apiManager = APIManager.getInstance();
    private static final LoggerManager log = LoggerManager.getInstance();

    @DataProvider(name = "data-provider")
    public Object[][] getData() {
        return new Object[][]
                {
                        {"Email", Constants.EMAILTEST, "fullName", Constants.FULLNAMETEST, "Password", Constants.PASSWORDTEST},
                        {"email", Constants.EMAILTEST, "FullName", Constants.FULLNAMETEST, "Password", Constants.PASSWORDTEST},
                        {"Email", Constants.EMAILTEST, "FullName", Constants.FULLNAMETEST, "password", Constants.PASSWORDTEST},
                        {"", "", "", "", "", ""}
                };
    }
    @Test(dataProvider = "data-provider")
    public void createUserDataDriven(String email, String dataEmail, String fullName, String dataFullName, String password, String dataPassword){
        log.info("Verify an invalid request when when performing a POST without a input parameters to \"https://todo.ly/api/user.json\"");
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put(email, dataEmail);
        jsonAsMap.put(fullName, dataFullName);
        jsonAsMap.put(password, dataPassword);

        Response response = apiManager.post(environment.getUserEndpoint(), ContentType.JSON, jsonAsMap);
        System.out.println(response.jsonPath().get().toString());
        System.out.println(jsonAsMap);

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNotNull(response.jsonPath().getString("ErrorMessage"), "Error Message was returned");
        Assert.assertNotNull(response.jsonPath().getString("ErrorCode"), "Error Code was returned");
        Assert.assertEquals("Invalid Request", response.jsonPath().getString("ErrorMessage"));
        Assert.assertEquals("101", response.jsonPath().getString("ErrorCode"));
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