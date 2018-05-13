package ru.stqa.training.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class Task17 {

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 15/*seconds*/);
    }

    public boolean isElementPresent(By locator) {
        try {
            wait.until((WebDriver d) -> d.findElement(locator));
            return true;
        } catch (TimeoutException ex) {
            return false;
        }
    }
    @Test
    public void task17() {

        String getCatalog = "http://localhost/litecart/admin/?app=catalog&doc=catalog&category_id=1";
        String checkCatalog = "//*[contains(@href, 'edit_product')][@class = 'button']";

        //login
        login();
        //go to catalog
        driver.get(getCatalog);
        //check that catalog is opened successfully
        assertTrue(isElementPresent(By.xpath(checkCatalog)));
        //expand all categories to find all products
        expandCategories();
        //find all products
        String find_products = "//*[contains(@href, 'product_id')][@title = 'Edit']";
        List<WebElement> products = driver.findElements(By.xpath(find_products));
        //cycle for all products
        for (int i=1;i <= products.size();i++) {
            // expand all categories because the list is expired
            expandCategories();
            //find all products
            List<WebElement> current_products = driver.findElements(By.xpath(find_products));
            //click on next product
            current_products.get(i-1).click();
            assertTrue(isElementPresent(By.xpath("//*[@href = '#tab-general']")));
            //get list of logs
            List<LogEntry> logEntries = driver.manage().logs().get("browser").getAll();
            //print number of records in log
            System.out.println("Product "+ i +": " + logEntries.size());
            //print log
            if (logEntries.size() != 0) {
                driver.manage().logs().get("browser").forEach(l -> System.out.println(l));
            }
            //return to Catalog
            driver.get(getCatalog);
            assertTrue(isElementPresent(By.xpath(checkCatalog)));

        }

    }

    public void expandCategories(){
        //find categories
        String find_categories = "//a[not(contains(@href, 'product_id'))][contains(@href,'catalog&category_id')][not(contains(@href, 'language'))]";
        List<WebElement> categories = driver.findElements(By.xpath(find_categories));
        //expand categories while it is possible
        if (categories.size() != 0){
            categories.get(0).click();
            moreExpandCategories(categories.size(), 0, find_categories);
        }
    }


    public void moreExpandCategories(int currentCategoriesSize, int i, String find_categories){
        //check that all categories are expand or expand categories more
        i++;
        List<WebElement> categories = driver.findElements(By.xpath(find_categories));
        if (categories.size() != currentCategoriesSize){
            categories.get(i).click();
            moreExpandCategories(categories.size(),i, find_categories);
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
