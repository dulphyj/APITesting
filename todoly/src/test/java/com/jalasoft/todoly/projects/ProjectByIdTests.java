package com.jalasoft.todoly.projects;

import api.APIManager;
import api.methods.APIProjectMethods;
import entities.projects.Project;
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

import static constants.Constants.INVALIDID21;

public class ProjectByIdTests {
    private static final Environment environment = Environment.getInstance();
    private static final APIManager apiManager = APIManager.getInstance();
    private static final LoggerManager log = LoggerManager.getInstance();
    private final ArrayList<Project> projects = new ArrayList<>();

    @BeforeClass
    public void setup() {
        apiManager.setCredentials(environment.getUserName(), environment.getPassword());
        projects.add(APIProjectMethods.createProject("ProjectById Test Project", 1));
        projects.add(APIProjectMethods.createProject("ProjectById Delete Test Project", 2));
        if ((projects.get(0) == null) || (projects.get(1) == null)) {
            Assert.fail("Projects were not created");
        }
    }

    @Test
    public void getProjectById() {
        log.info("GET: Verify that is possible to get a specific project by ID");
        Project project = projects.get(0);
        String projectByIdEndpoint = String.format(environment.getProjectByIdEndpoint(), project.getId());
        Response response = apiManager.get(projectByIdEndpoint);
        Project responseProject = response.as(Project.class);

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNull(response.jsonPath().getString("ErrorMessage"), "Error Message was returned");
        Assert.assertNull(response.jsonPath().getString("ErrorCode"), "Error Code was returned");
        Assert.assertEquals(responseProject.getId(), project.getId(), "Id value is incorrect");
        Assert.assertEquals(responseProject.getContent(), project.getContent(), "Content value is incorrect");
        Assert.assertEquals(responseProject.getIcon(), project.getIcon(), "Icon value is incorrect");
    }

    @Test
    public void updateProjectById() {
        log.info("PUT: Verify that is possible to update specific fields of a project by Id");
        Project project = projects.get(0);
        String projectByIdEndpoint = String.format(environment.getProjectByIdEndpoint(), project.getId());
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("Icon", 3);

        Response response = apiManager.put(projectByIdEndpoint, ContentType.JSON, jsonAsMap);
        Project responseProject = response.as(Project.class);

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNull(response.jsonPath().getString("ErrorMessage"), "Error Message was returned");
        Assert.assertNull(response.jsonPath().getString("ErrorCode"), "Error Code was returned");
        Assert.assertEquals(responseProject.getIcon(), jsonAsMap.get("Icon"), "Incorrect Icon value was set");
    }

    @Test
    public void updateProjectByInvalidId() {
        log.info("PUT: Verify that is not possible to update an invalid project Id");
        String projectByIdEndpoint = String.format(environment.getProjectByIdEndpoint(), INVALIDID21);
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("Icon", 3);

        Response response = apiManager.put(projectByIdEndpoint, ContentType.JSON, jsonAsMap);
        System.out.println(response);

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNotEquals(response.body().asString(), "", "Body is empty");
        Assert.assertNotNull(response.jsonPath().getString("ErrorMessage"), "Error Message was not returned");
        Assert.assertNotNull(response.jsonPath().getString("ErrorCode"), "Error Code was not returned");
        Assert.assertEquals(response.jsonPath().getString("ErrorMessage"), "Invalid Project Id", "Incorrect Error Message was returned");
        Assert.assertEquals(response.jsonPath().getString("ErrorCode"), "304", "Incorrect Error Code was returned");
    }

    @Test
    public void deleteProjectById() {
        log.info("DELETE: Verify that is possible to delete a project by id");
        Project project = projects.get(1);
        String projectByIdEndpoint = String.format(environment.getProjectByIdEndpoint(), project.getId());
        Response response = apiManager.delete(projectByIdEndpoint);
        Project responseProject = response.as(Project.class);

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNull(response.jsonPath().getString("ErrorMessage"), "Error Message was returned");
        Assert.assertNull(response.jsonPath().getString("ErrorCode"), "Error Code was returned");
        Assert.assertTrue(responseProject.getDeleted(), "Project was not deleted");
    }

    @AfterClass
    public void teardown() {
        for (Project project : projects) {
            boolean isProjectDeleted = APIProjectMethods.deleteProject(project.getId());
            Assert.assertTrue(isProjectDeleted, "Project was not deleted");
        }
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
