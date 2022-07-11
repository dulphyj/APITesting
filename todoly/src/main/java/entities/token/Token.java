package entities.token;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class Token {

    @JsonProperty("TokenString")
    private String tokenString;
    @JsonProperty("UserEmail")
    private String userEmail;
    @JsonProperty("ExpirationTime")
    private String expirationTime;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("TokenString")
    public String getTokenString() {
        return tokenString;
    }

    @JsonProperty("TokenString")
    public void setTokenString(String tokenString) {
        this.tokenString = tokenString;
    }

    @JsonProperty("UserEmail")
    public String getUserEmail() {
        return userEmail;
    }

    @JsonProperty("UserEmail")
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @JsonProperty("ExpirationTime")
    public String getExpirationTime() {
        return expirationTime;
    }

    @JsonProperty("ExpirationTime")
    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
