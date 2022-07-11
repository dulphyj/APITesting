package framework;

import utils.LoggerManager;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Environment {
    private Properties properties;
    private static final LoggerManager log = LoggerManager.getInstance();
    private static Environment instance;

    private Environment() {
        initialize();
    }

    public static Environment getInstance() {
        if (instance == null) {
            instance = new Environment();
        }
        return instance;
    }

    private void initialize() {
        log.info("Reading properties file");
        properties = new Properties();
        try {
            File file = new File("gradle.properties");
            FileReader fileReader = new FileReader(file);
            properties.load(fileReader);
            fileReader.close();
        } catch (IOException e) {
            log.error("unable to read properties file");
        }
    }

    private String getEnvironmentSetting(String setting) {
        return (String) getInstance().properties.get(setting);
    }

    public String getBaseURL() {
        return getEnvironmentSetting("baseURL");
    }

    public String getUserName() {
        return getEnvironmentSetting("userName");
    }

    public String getPassword() {
        return getEnvironmentSetting("password");
    }

    public String getBasePath() {
        return  getEnvironmentSetting("basePath");
    }

    public String getProjectsEndpoint() {
        return getEnvironmentSetting("projectsEndpoint");
    }

    public String getProjectByIdEndpoint() {
        return getEnvironmentSetting("projectByIdEndpoint");
    }

    public String getInvalidUserName() {
        return getEnvironmentSetting("invalidUserName");
    }

    public String getInvalidPassword() {
        return getEnvironmentSetting("invalidPassword");
    }

    public String getUserEndpoint() {
        return getEnvironmentSetting("userEndpoint");
    }

    public String getUserDeleteEndpoint() {
        return getEnvironmentSetting("userDeleteEndpoint");
    }

    public String getUserUpdateEndpoint() {
        return getEnvironmentSetting("userDeleteEndpoint");
    }

    public String getFilterEndpoint() {
        return  getEnvironmentSetting("filtersEndpoint");
    }

    public String getFilterByIdEndpoint() {
        return  getEnvironmentSetting("filterByIdEndpoint");
    }

    public String getItemsOfAFilter() {
        return getEnvironmentSetting("itemsOfAFilter");
    }

    public String getItemsEndPoint() {
        return getEnvironmentSetting("itemsEndPoint");
    }

    public String getItemByIdEndPoint() {
        return getEnvironmentSetting("itemByIdEndpoint");
    }
    public String getDoneItemsEndPoint() {
        return getEnvironmentSetting("doneItemsOfFilter");
    }

    public String getItemsOfInboxFilterByIdEndpoint() {
        return getEnvironmentSetting("ItemsOfInboxFilterByIdEndpoint");
    }
    public String getItemsOfTodayFilterByIdEndpoint() {
        return getEnvironmentSetting("ItemsOfTodayFilterByIdEndpoint");
    }
    public String getItemsOfNextFilterByIdEndpoint() {
        return getEnvironmentSetting("ItemsOfNextFilterByIdEndpoint");
    }

    public String getIconByIdEndpoint() {
        return getEnvironmentSetting("iconByIdEndpoint");
    }

    public String isAuthenticatedEndpoint() {
        return getEnvironmentSetting("isAuthenticatedEndpoint");
    }

    public String getTokenEndpoint() {
        return getEnvironmentSetting("tokenEndpoint");
    }
}
