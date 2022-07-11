package api.methods;

import entities.items.Item;
import entities.items.NewItem;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class APIItemMethods extends Method {
    public static Item createItem(String content) {
        NewItem item = new NewItem(content);
        String itemsEndPoint = environment.getItemsEndPoint();
        Response response = apiManager.post(itemsEndPoint, ContentType.JSON, item);
        Item responseItem = response.as(Item.class);

        if (responseItem.getId() != null) {
            return responseItem;
        } else {
            log.error("Unable to create a new item");
            return null;
        }
    }

    public static boolean deleteItem(int itemId) {
        log.info("Deleting Item " + itemId);
        String itemByIdEndpoint = String.format(environment.getItemByIdEndPoint(), itemId);
        Response response = apiManager.delete(itemByIdEndpoint);
        Item responseItem = response.as(Item.class);

        if (responseItem.getDeleted() != null) {
            return responseItem.getDeleted();
        } else {
            log.error("Unable to complete request to delete Item " + itemId);
            return false;
        }
    }
}
