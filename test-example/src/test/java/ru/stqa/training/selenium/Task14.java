package ru.stqa.training.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class Task14 {

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 15/*seconds*/);
    }

    @Test
    public void task14() {
        //login
        login();
        //go to Countries
        driver.get("http://localhost/litecart/admin/?app=countries&doc=countries");
        String button_edit_country = "//*[@class = 'button'][contains(@href, 'edit_country')]";
        assertTrue(isElementPresent(By.xpath(button_edit_country)));
        //find and click on button "Add New Country"
        driver.findElement(By.xpath(button_edit_country)).click();
        assertTrue(isElementPresent(By.name("iso_code_2")));
        //get external links
        List<WebElement> links = driver.findElements(By.xpath("//*[contains(@class,'external-link')]"));
        for (WebElement link: links){
            //save current window id
            String mainWindow = driver.getWindowHandle();
            //save ids opened windows
            Set<String> oldWindows = driver.getWindowHandles();
            //go on link
            link.click();
            //wait new window
            String newWindow = wait.until(thereIsWindowOtherThan(oldWindows));
            //switch to window
            driver.switchTo().window(newWindow);
            //close new window
            driver.close();
            //switch to main window
            driver.switchTo().window(mainWindow);
        }
    }

    //first version of function thereIsWindowOtherThan withoutreplacement of an anonymous class with lambda.
    //IntelliJ IDEA suggested using the replacement of an anonymous class with lambda.

    /* public ExpectedCondition<String> thereIsWindowOtherThan(Set<String> windows){
        return new ExpectedCondition<String>() {
            public String apply(WebDriver dr) {
                Set<String> handles = dr.getWindowHandles();
                handles.removeAll(windows);
                return handles.size() > 0? handles.iterator().next():null;
            }
        };
    }*/

    //second version of function thereIsWindowOtherThan with the replacement of an anonymous class with lambda
    public ExpectedCondition<String> thereIsWindowOtherThan(Set<String> windows){

        return (WebDriver dr) -> {
                Set<String> handles = dr.getWindowHandles();
                handles.removeAll(windows);
                return handles.size() > 0? handles.iterator().next():null;
             };

    }

    public boolean isElementPresent(By locator) {
        try {
            wait.until((WebDriver d) -> d.findElement(locator));
            return true;
        } catch (TimeoutException ex) {
            return false;
        }
    }

    public void login(){

        driver.get("http://localhost/litecart/admin/");
        WebElement username = driver.findElement(By.name("username"));
        username.sendKeys("admin");
        WebElement password = driver.findElement(By.name("password"));
        password.sendKeys("admin");
        driver.findElement(By.name("remember_me")).click();
        driver.findElement(By.name("login")).click();
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
