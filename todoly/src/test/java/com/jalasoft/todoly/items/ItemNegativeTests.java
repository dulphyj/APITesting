package com.jalasoft.todoly.items;

import api.APIManager;
import api.methods.APIItemMethods;
import constants.Constants;
import entities.items.Item;
import entities.items.NewItem;
import entities.projects.Project;
import framework.Environment;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItemNegativeTests {

    private static final Environment environment = Environment.getInstance();
    private static final APIManager apiManager = APIManager.getInstance();
    private final ArrayList<Item> items = new ArrayList<>();

    @BeforeClass
    public void setup() {
        apiManager.setCredentials(environment.getUserName(), environment.getPassword());
        items.add(APIItemMethods.createItem("Salome"));
        items.add(APIItemMethods.createItem("Vicente"));
        if ((items.get(0) == null) || (items.get(1) == null)) {
            Assert.fail("Items were not created");
        }
    }

    @Test
    public void getItemByNonExistentId() {
        String itemByIdEndpoint = String.format(environment.getItemByIdEndPoint(), Constants.INVALIDITEMID);
        Response response = apiManager.get(itemByIdEndpoint);
        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNull(response.jsonPath().getString("ErrorMessage"), "Error Message was returned");
        Assert.assertNull(response.jsonPath().getString("ErrorCode"), "Error Code was returned");
        Assert.assertEquals("Invalid Id", response.jsonPath().getString("ErrorMessage"));
        Assert.assertEquals("301", response.jsonPath().getString("ErrorCode"));
    }

    @Test
    public void createNewItemWithId() {
        NewItem newItem = new NewItem(Constants.INVALIDITEMID, "Test Item");
        Response response = apiManager.post(environment.getItemsEndPoint(), ContentType.JSON, newItem);
        Project responseProject = response.as(Project.class);
        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNull(response.jsonPath().getString("ErrorMessage"), "Error Message was returned");
        Assert.assertNull(response.jsonPath().getString("ErrorCode"), "Error Code was returned");
        Assert.assertNotEquals(responseProject.getContent(), newItem.getContent(), "Incorrect Content value was set");
        Assert.assertEquals("Invalid Request", response.jsonPath().getString("ErrorMessage"));
        Assert.assertEquals("101", response.jsonPath().getString("ErrorCode"));
    }

    @Test
    public void updateItemById() {
        Item item = items.get(0);
        String itemByIdEndpoint = String.format(environment.getItemByIdEndPoint(), item.getId());
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("Id",Constants.INVALIDITEMID);
        Response response = apiManager.put(itemByIdEndpoint, ContentType.JSON, jsonAsMap);
        Item responseItem = response.as(Item.class);
        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNull(response.jsonPath().getString("ErrorMessage"), "Error Message was returned");
        Assert.assertNull(response.jsonPath().getString("ErrorCode"), "Error Code was returned");
        Assert.assertEquals(responseItem.getId(), jsonAsMap.get("Id"), "Incorrect COntent value was set");
        Assert.assertEquals("Invalid Request", response.jsonPath().getString("ErrorMessage"));
        Assert.assertEquals("101", response.jsonPath().getString("ErrorCode"));
    }
}