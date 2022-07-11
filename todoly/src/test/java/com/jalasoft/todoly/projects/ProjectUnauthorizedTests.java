package com.jalasoft.todoly.projects;

import api.APIManager;
import entities.projects.NewProject;
import framework.Environment;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.LoggerManager;

public class ProjectUnauthorizedTests {
    private static final Environment environment = Environment.getInstance();
    private static final APIManager apiManager = APIManager.getInstance();
    private static final LoggerManager log = LoggerManager.getInstance();

    @BeforeClass
    public void setup() {
        apiManager.setCredentials(environment.getInvalidUserName(), environment.getInvalidPassword());
    }

    @Test
    public void getAllProjects() {
        log.info("GET: Verify that is not possible to get all projects without authentication");
        Response response = apiManager.get(environment.getProjectsEndpoint());
        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertTrue(response.getBody().asString().contains("\"ErrorMessage\":\"Not Authenticated\""), "Error Message is not returned");
        Assert.assertTrue(response.getBody().asString().contains("\"ErrorCode\":102"), "Error Code is not returned");
    }

    @Test
    public void createNewProject() {
        log.info("POST: Verify that is not possible to create a new project without authentication");
        NewProject newProject = new NewProject("My Testing Project", 2);
        Response response = apiManager.post(environment.getProjectsEndpoint(), ContentType.JSON, newProject);
        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNotNull(response.jsonPath().getString("ErrorMessage"), "Error Message was not returned");
        Assert.assertNotNull(response.jsonPath().getString("ErrorCode"), "Error Code was not returned");
        Assert.assertEquals(response.jsonPath().getString("ErrorMessage"), "Not Authenticated", "Incorrect Error Message was returned");
        Assert.assertEquals(response.jsonPath().getString("ErrorCode"), "102", "Incorrect Error Code was returned");
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
