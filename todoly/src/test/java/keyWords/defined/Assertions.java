package keyWords.defined;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.io.IOException;
import java.util.Properties;

public class Assertions {
    public void AssertElement(String assertionType, String objectName, String locatorType) throws IOException {
        switch (AssertionType) {
            case "Displayed":
                Assert.assertEquals(true, driven.findElement(this.getObject(objectName, locatorType)).isDislayed());
                break;
            case "Enabled":
                Assert.assertEquals(true, driven.findElement(this.getObject(objectName, locatorType)).isEnamble());
                break;
            case "Selected":
                Assert.assertEquals(true, driven.findElement(this.getObject(objectName, locatorType)).isSelected());
                break;
        }
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
