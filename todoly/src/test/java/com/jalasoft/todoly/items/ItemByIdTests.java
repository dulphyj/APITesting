package com.jalasoft.todoly.items;

import api.APIManager;
import api.methods.APIItemMethods;
import entities.items.Item;
import framework.Environment;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItemByIdTests {
    private static final Environment environment = Environment.getInstance();
    private static final APIManager apiManager = APIManager.getInstance();
    private final ArrayList<Item> items = new ArrayList<>();

    private int id;

    @BeforeClass
    public void setup() {
        apiManager.setCredentials(environment.getUserName(), environment.getPassword());

        items.add(APIItemMethods.createItem("Item test 1"));
        items.add(APIItemMethods.createItem("Item test 2"));

        if ((items.get(0) == null) || (items.get(1) == null)) {
            Assert.fail("Items were not created");
        }
    }

    @Test
    public void getItemById() {
        Item item = items.get(0);
        id = item.getId();
        String itemByIdEndpoint = String.format(environment.getItemByIdEndPoint(), id);
        System.out.println("THE ID IS: " + id);
        Response response = apiManager.get(itemByIdEndpoint);
        Item responseItem = response.as(Item.class);

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNull(response.jsonPath().getString("ErrorMessage"), "Error Message was returned");
        Assert.assertNull(response.jsonPath().getString("ErrorCode"), "Error Code was returned");
        Assert.assertEquals(responseItem.getId(), item.getId(), "Id value is incorrect");
        Assert.assertEquals(responseItem.getContent(), item.getContent(), "Content value is incorrect");

    }
    @Test
    public void updateItemById() {
        Item item = items.get(0);
        String itemByIdEndpoint = String.format(environment.getItemByIdEndPoint(), item.getId());
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("Content", "Item test 3");

        Response response = apiManager.put(itemByIdEndpoint, ContentType.JSON, jsonAsMap);
        Item responseItem = response.as(Item.class);

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNull(response.jsonPath().getString("ErrorMessage"), "Error Message was returned");
        Assert.assertNull(response.jsonPath().getString("ErrorCode"), "Error Code was returned");
        Assert.assertEquals(responseItem.getContent(), jsonAsMap.get("Content"), "Incorrect COntent value was set");
    }
    @Test
    public void deleteItemById() {
        Item item = items.get(1);
        String itemByIdEndpoint = String.format(environment.getItemByIdEndPoint(), item.getId());
        Response response = apiManager.delete(itemByIdEndpoint);
        Item responseItem = response.as(Item.class);

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNull(response.jsonPath().getString("ErrorMessage"), "Error Message was returned");
        Assert.assertNull(response.jsonPath().getString("ErrorCode"), "Error Code was returned");
        Assert.assertTrue(responseItem.getDeleted(), "Item was not deleted");
    }
    @AfterClass
    public void teardown() {
        for (Item item : items) {
            boolean isItemDeleted = APIItemMethods.deleteItem(item.getId());
            Assert.assertTrue(isItemDeleted, "Item was not deleted");
        }
    }
}
