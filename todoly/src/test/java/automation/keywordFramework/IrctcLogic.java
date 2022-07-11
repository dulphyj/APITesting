package automation.keywordFramework;

import keyWords.defined.Assertions;
import keyWords.defined.Keywords;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;

public class IrctcLogic {
    Keywords keywords;
    WebDriver driven;
    String path = System.getProperty("user.dir");

    @Test
    public void  readExcelAndExecute() throws IOException, InterruptedException {
        String excelFilePath = "testExcel.xlsx";
        FileInputStream fileInputStream = new FileInputStream(excelFilePath);
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        int testCaseCount = workbook.getNumberOfSheets()-1;
        System.out.println("total tc" + testCaseCount);
        for (int testcase = 0; testcase < testCaseCount; testcase++) {
            System.setProperty("webdriven.chrome.driven", path+"\\Drivers\\chromedriver.exe");
            driven = new ChromeDriver();
            XSSFSheet worksheet = workbook.getSheetAt(testcase);
            System.out.println("worksheet number"+ testcase +" "+ worksheet.getSheetName());
            int row = worksheet.getLastRowNum();
            int column = worksheet.getRow(0).getLastCellNum();
            for(int i = 1; i <= row; i++){
                LinkedList<String> testExecution = new LinkedList<>();
                System.out.println("Row value :"+i+"it has first cell value as: "+worksheet.getRow(i).getCell(0));
                for(int j=0; j<column-1; j++){
                    System.out.println("Column index:"+j);
                    Cell criteria = worksheet.getRow(i).getCell(j);
                    String criterialText;
                    if(criteria==null){
                      criterialText = null;
                    } else {
                        criterialText = criteria.getStringCellValue();
                    }
                    testExecution.add(criterialText);
                }
                System.out.println("List: "+testExecution);
                String testStep= testExecution.get(0);
                String objectName= testExecution.get(1);
                String locatorType= testExecution.get(2);
                String testData= testExecution.get(3);
                String assertionType= testExecution.get(4);
                String expectedValue= testExecution.get(5);
                String actualValue= testExecution.get(6);
                perform(testStep, objectName, locatorType, testData, assertionType, expectedValue, actualValue);
                System.out.println("Row"+i+" is read");
            }

            driven.close();
        }
    }

    public void perform(String operation, String testStep, String objectName, String locatorType, String testData, String assertionType, String expectedValue, String actualValue) throws IOException, InterruptedException {
        switch (operation){
            case "enter_URL":
                keywords.enter_URL(driven, testData);
                break;
            case "get_CurrentURL":
                keywords.getCurrentURL(driven);
                break;
            case "type":
                keywords.type(driven, objectName, locatorType, testData);
                break;
            case "click":
                keywords.click(driven, objectName, locatorType);
                break;
            case "wait":
                keywords.wait(driven, objectName,locatorType);
                break;
            case "imlicitWait":
                Thread.sleep(8000);
            default:
                break;
        }
        if(operation.contains("AssertURL")){
            switch (assertionType){
                case "contains":
                    assertion.AssertURLContains(driven, keywords.getCurrentURL(driven), expectedValue);
                case"equals":
                    assertion.AssertURLEquals(driven, keywords.getCurrentURL(driven), expectedValue);
            }
        }
        if(operation.contains("AssertElement")){
            assertion.AssertElement(driven, assertionType, objectName, locatorType);
        }
    }
}
