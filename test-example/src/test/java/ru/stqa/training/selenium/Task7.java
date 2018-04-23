package ru.stqa.training.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class Task7 {
    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 5/*seconds*/);
    }

    @Test
    public void task7() {

        //xpath expression
        String xPathListMenu = "//*[@id = 'app-'][";
        String xPathListSubmenu =  "//*[starts-with(@id,'doc-')][";
        String xPathLink = "]/a[starts-with(@href,'http://localhost/litecart/admin')]";
        String xPathSelected = "][@class = 'selected']";

        //login
        driver.get("http://localhost/litecart/admin/");
        WebElement username = driver.findElement(By.name("username"));
        username.sendKeys("admin");
        WebElement password = driver.findElement(By.name("password"));
        password.sendKeys("admin");
        driver.findElement(By.name("remember_me")).click();
        driver.findElement(By.name("login")).click();


        assertTrue(isElementPresent(By.id("box-apps-menu")));//check that the login was successful
        WebElement menu = driver.findElement(By.id("box-apps-menu"));
        List<WebElement> m_list = menu.findElements(By.id("app-"));// list of parent elements
        System.out.println("Size of elements in menu: "+ m_list.size());

        for (int i=1;i <= m_list.size();i++) {
            driver.findElement(By.xpath(xPathListMenu + i + xPathLink)).click();
            assertTrue(isElementPresent(By.xpath(xPathListMenu + i + xPathSelected)));// check that the desired menu item is selected
            assertTrue(isElementPresent(By.tagName("h1")));
            List<WebElement> d_list = driver.findElements(By.xpath("//*[starts-with(@id,'doc-')]"));//list of internal elements for some parent element
            String currentParentUrl = driver.getCurrentUrl();
            System.out.println("--------------------------------------------------------------------------");
            System.out.println("Parent URL " + i + ": " + currentParentUrl);
            System.out.println("Size of internal elements: " + d_list.size());
            for (int i1=1;i1<=d_list.size();i1++){
                driver.findElement(By.xpath(xPathListSubmenu + i1 + xPathLink)).click();
                assertTrue(isElementPresent(By.xpath(xPathListSubmenu + i1 + xPathSelected)));// check that the desired submenu item is selected
                assertTrue(isElementPresent(By.tagName("h1")));
                String currentUrl = driver.getCurrentUrl();
                System.out.println("Internal URL "+ i1 + ": " + currentUrl);
            }
            driver.get("http://localhost/litecart/admin/");
        }

    }

    public boolean isElementPresent(By locator) {
        try {
            wait.until((WebDriver d) -> d.findElement(locator));
            return true;
        } catch (TimeoutException ex) {
            return false;
        }
    }
    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
