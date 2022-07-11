package com.jalasoft.todoly.projects;

import api.APIManager;
import api.methods.APIProjectMethods;
import entities.projects.NewProject;
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
import java.util.Objects;

public class ProjectTests {
    private static final Environment environment = Environment.getInstance();
    private static final APIManager apiManager = APIManager.getInstance();
    private static final LoggerManager log = LoggerManager.getInstance();
    private final ArrayList<Integer> projectIds = new ArrayList<>();

    @BeforeClass
    public void setup() {
        apiManager.setCredentials(environment.getUserName(), environment.getPassword());
    }

    @Test
    public void getAllProjects() {
        log.info("GET: Verify that is possible to return the list of all projects of an authenticated user");
        Response response = apiManager.get(environment.getProjectsEndpoint());

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertFalse(response.getBody().asString().contains("ErrorMessage"), "Correct response body is returned");
        Assert.assertFalse(response.getBody().asString().contains("ErrorCode"), "Correct response body is not returned");
    }

    @Test
    public void createNewProject() {
        log.info("POST: Verify that is possible to create a new project");
        NewProject newProject = new NewProject("My Testing Project", 2);
        Response response = apiManager.post(environment.getProjectsEndpoint(), ContentType.JSON, newProject);
        Project responseProject = response.as(Project.class);
        projectIds.add(responseProject.getId());

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNull(response.jsonPath().getString("ErrorMessage"), "Error Message was returned");
        Assert.assertNull(response.jsonPath().getString("ErrorCode"), "Error Code was returned");
        Assert.assertEquals(responseProject.getContent(), newProject.getContent(), "Incorrect Content value was set");
        Assert.assertEquals(responseProject.getIcon(), newProject.getIcon(), "Incorrect Icon value was set");
    }

    @Test
    public void tooShortProjectName() {
        log.info("POST: Verify that is not possible to create a project with a too short name");
        NewProject newProject = new NewProject("", 2);
        Response response = apiManager.post(environment.getProjectsEndpoint(), ContentType.JSON, newProject);

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNotNull(response.jsonPath().getString("ErrorMessage"), "Error Message was not returned");
        Assert.assertNotNull(response.jsonPath().getString("ErrorCode"), "Error Code was not returned");
        Assert.assertEquals(response.jsonPath().getString("ErrorMessage"), "Too Short Project Name", "Incorrect Error Message was returned");
        Assert.assertEquals(response.jsonPath().getString("ErrorCode"), "305", "Incorrect Error Code was returned");
    }

    @Test
    public void contentLowerCaseBug() {
        log.info("POST: Verify that is not possible to create a project with content field instead of Content");
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("content", "My Testing Project");
        jsonAsMap.put("Icon", 3);

        Response response = apiManager.post(environment.getProjectsEndpoint(), ContentType.JSON, jsonAsMap);
        Project responseProject = response.as(Project.class);
        projectIds.add(responseProject.getId());

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNotNull(response.jsonPath().getString("ErrorMessage"), "Error Message was returned");
        Assert.assertNotNull(response.jsonPath().getString("ErrorCode"), "Error Code was returned");
        Assert.assertEquals("Invalid Request", response.jsonPath().getString("ErrorMessage"));
        Assert.assertEquals("101", response.jsonPath().getString("ErrorCode"));
    }

    @AfterClass
    public void teardown() {
        projectIds.removeIf(Objects::isNull);
        if (projectIds.size() > 0) {
            for (int projectId : projectIds) {
                boolean isProjectDeleted = APIProjectMethods.deleteProject(projectId);
                Assert.assertTrue(isProjectDeleted, "Project was not deleted");
            }
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
