package api;

import framework.Environment;
import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.LoggerManager;

public class APIManager {
    private static final LoggerManager log = LoggerManager.getInstance();
    private static APIManager instance;

    private APIManager() {
        initialize();
    }

    public static APIManager getInstance() {
        if (instance == null) {
            instance = new APIManager();
        }
        return instance;
    }

    private void initialize() {
        log.info("Initializing API Manager");
        RestAssured.baseURI = Environment.getInstance().getBaseURL();
        RestAssured.basePath = Environment.getInstance().getBasePath();
    }

    public void setCredentials(String userName, String password) {
        log.info("Setting credentials");
        PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
        authScheme.setUserName(userName);
        authScheme.setPassword(password);
        RestAssured.authentication = authScheme;
    }

    public Response get(String endpoint) {
        log.info("Executing GET");
        return RestAssured.given().get(endpoint);
    }

    public Response post(String endpoint, ContentType contentType, Object object) {
        log.info("Executing POST");
        return RestAssured.given().contentType(contentType).body(object).post(endpoint);
    }

    public Response delete(String endpoint) {
        log.info("Executing DELETE");
        return  RestAssured.given().delete(endpoint);
    }

    public Response put(String endpoint, ContentType contentType, Object object) {
        log.info("Executing PUT");
        return RestAssured.given().contentType(contentType).body(object).put(endpoint);
    }

    public Response getWithToken(String endpoint, String headerKey, String headerValue) {
        log.info("Executing GET with token");
        return  RestAssured.given().header(headerKey, headerValue).when().get(endpoint);
    }
    public Response deleteWithToken(String endpoint, String headerKey, String headerValue) {
        log.info("Executing DELETE with token");
        return  RestAssured.given().header(headerKey, headerValue).when().delete(endpoint);
    }
}
