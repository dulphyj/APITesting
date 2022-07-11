package keyWords.defined;

import framework.Environment;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.Properties;

public class Keywords {

    private Environment environment = Environment.getInstance();
    WebDriver driver;

    public void enter_URL(WebDriver driven, String testData) {
        driven.get(testData);
    }

    public void type(WebDriver driven, String objectName, String locatorType, String testData) throws IOException{
        driven.findElements(this.getObject(objectName, locatorType)).sendKeys(testData);
    }

    public void wait(WebDriver driven, String objectName, String locatorType) throws IOException{
        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until(ExpectedCondition.visibilityOf(driver.findElements(this.getObject(objectName, locatorType))));
    }


    public void click(WebDriver driven,String objectName, String locatorType) throws IOException {
        driver.findElements(this.getObject(objectName, locatorType)).click();
    }

    public String getCurrentURL(WebDriver driver){
        String URL = driver.getCurrentUrl();
        return URL;
    }

    public By getObject(String objectName, String locatorType) {
        String path = System.getProperty("user.dir");
        Properties properties = new Properties();
        properties.getProperty("userName");
        if (locatorType.equalsIgnoreCase("XPATH")) {
            return By.xpath(properties.getProperty(objectName));
        } else if (locatorType.equalsIgnoreCase("CLASNAME")) {
            return By.className(properties.getProperty(objectName));
        } else if (locatorType.equalsIgnoreCase("NAME")) {
            return By.name(properties.getProperty(objectName));
        } else if (locatorType.equalsIgnoreCase("CSS")) {
            return By.cssSelector(properties.getProperty(objectName));
        } else if (locatorType.equalsIgnoreCase("LINK")) {
            return By.linkText(properties.getProperty(objectName));
        } else if(locatorType.equalsIgnoreCase("PARTIALLINK")){
            return By.partialLinkText(properties.getProperty(objectName));
        }
    }

}
