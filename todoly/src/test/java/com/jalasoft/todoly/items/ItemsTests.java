package com.jalasoft.todoly.items;

import api.APIManager;
import api.methods.APIItemMethods;
import entities.items.Item;
import entities.items.NewItem;
import framework.Environment;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.Objects;

public class ItemsTests {
    private static final Environment environment = Environment.getInstance();
    private static final APIManager apiManager = APIManager.getInstance();
    private ArrayList<Integer> itemIds = new ArrayList<>();

    @BeforeClass
    public void setup() {
        apiManager.setCredentials(environment.getUserName(), environment.getPassword());
    }

    @Test
    public void getAllItems() {
        Reporter.log("Verify that a 200 OK status code and a correct response body result when a GET request to the \"/projects.json\" endpoint is executed", true);
        Response response = apiManager.get(environment.getItemsEndPoint());

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertFalse(response.getBody().asString().contains("ErrorMessage"), "Correct response body is returned");
        Assert.assertFalse(response.getBody().asString().contains("ErrorCode"), "Correct response body is not returned");
    }
    @Test
    public void createNewItem() {
        NewItem newItem = new NewItem("Item test");
        Response response = apiManager.post(environment.getItemsEndPoint(), ContentType.JSON, newItem);
        Item responseItem = response.as(Item.class);
        itemIds.add(responseItem.getId());

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNull(response.jsonPath().getString("ErrorMessage"), "Error Message was returned");
        Assert.assertNull(response.jsonPath().getString("ErrorCode"), "Error Code was returned");
        Assert.assertEquals(responseItem.getContent(), newItem.getContent(), "Incorrect Content value was set");
    }
    @AfterClass
    public void teardown() {
        itemIds.removeIf(Objects::isNull);
        if (itemIds.size() > 0) {
            for (int itemId : itemIds) {
                boolean isItemDeleted = APIItemMethods.deleteItem(itemId);
                Assert.assertTrue(isItemDeleted, "Item was not deleted");
            }
        }
    }
}
