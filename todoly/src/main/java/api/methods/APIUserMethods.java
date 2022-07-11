package api.methods;

import entities.user.NewUser;
import entities.user.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class APIUserMethods extends Method {

    public static User createUser(String email, String fullName, String password) {
        NewUser user = new NewUser(email, fullName, password);
        String userEndpoint = environment.getUserEndpoint();
        Response response = apiManager.post(userEndpoint, ContentType.JSON, user);
        User responseUser = response.as(User.class);

        if (responseUser.getId() != null) {
            return  responseUser;
        } else {
            log.error("Unable to create a new user");
            return null;
        }
    }

    public static boolean deleteUser(String email, String password) {
        String userEndpoint = String.format(environment.getUserDeleteEndpoint());
        apiManager.setCredentials(email, password);
        Response response = apiManager.delete(userEndpoint);
        return true;
    }

}
