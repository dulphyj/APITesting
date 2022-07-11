package api.methods;

import entities.token.Token;
import io.restassured.response.Response;

public class APITokenMethods extends Method {

    public static boolean deleteToken(String tokenString) {
        String tokenEndpoint = String.format(environment.getTokenEndpoint());
        Response response = apiManager.deleteWithToken(tokenEndpoint, "Token", tokenString);
        log.info("Deleting Token " + tokenString);
        Token responseToken = response.as(Token.class);
        if (responseToken.getUserEmail() != null) {
            return true;
        } else {
            log.error("Unable to complete request to delete Token " + tokenString);
            return false;
        }
    }
}
