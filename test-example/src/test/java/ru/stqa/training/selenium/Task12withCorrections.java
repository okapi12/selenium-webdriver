package ru.stqa.training.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.util.List;

public class Task12withCorrections {

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 5/*seconds*/);
    }

    @Test
    public void task12() {

        String qty_products = "//*[contains(@href, 'product_id')][@title = 'Edit']";

        login();

        //go to menu 'Catalog'
        driver.findElement(By.xpath("//a[contains(@href, 'catalog')]")).click();

        //check that menu 'Catalog' is opened
        String add_product = "//a[contains(@href, 'edit_product')]";
        assertTrue(isElementPresent(By.xpath(add_product)));

        expandElements();
        List<WebElement> current_qty_products = driver.findElements(By.xpath(qty_products));

        //go to product creation
        driver.findElement(By.xpath(add_product)).click();

        //check that page with active tab 'General' is opened
        assertTrue(isElementPresent(By.xpath("//li[@class = 'active']/a[@href ='#tab-general']")));

        String[][] tab_general = {
                {"name[en]", "Ugly duck " + RandomStringUtils.randomNumeric(3), "false"},
                {"code", RandomStringUtils.randomNumeric(3), "false"},
                {"quantity", "5.00", "true"},
                {"date_valid_from", "01.05.2018", "false"},
                {"date_valid_to", "05.05.2018", "false"}
        };

        setFields(tab_general);

        //filling in the fields on the tab 'General'
        driver.findElement(By.xpath("//*[@name = 'status'][@value = 1]")).click(); //status = 'Enabled'

        //select all categories
        for (int i=0;i <= 2;i++)
        {
            Boolean option_exist = isElementPresent(By.xpath("//select[@name = 'default_category_id']/option[@value = "+i+"]"));
            if (option_exist == false){
                driver.findElement(By.xpath("//input[@name = 'categories[]'][@value="+i+"]")).click();
            }
        }
        List<WebElement> options = driver.findElements(By.xpath("//select[@name = 'default_category_id']/option"));
        assertTrue(options.size() == 3);

        setSelect("default_category_id","Subcategory");

        //select all genders
        List<WebElement> gender = driver.findElements(By.xpath("//input[@name = 'product_groups[]']"));
        for (WebElement gend: gender){
            gend.click();
        }

        //add image
        File file = new File("src/test/resources/ugly_duck.png");
        driver.findElement(By.name("new_images[]")).sendKeys(file.getAbsolutePath());

        //filling in the fields on the tab 'Information'
        driver.findElement(By.xpath("//*[@href = '#tab-information']")).click();
        assertTrue(isElementPresent(By.xpath("//li[@class = 'active']/a[@href ='#tab-information']")));

        String[][] tab_information = {
                {"keywords", "keyword", "false"},
                {"short_description[en]", "Ugly duck", "false"},
                {"head_title[en]", "Ugly duck", "false"},
                {"meta_description[en]", "New ugly duck", "false"}
        };

        setFields(tab_information);
        setSelect("manufacturer_id", "ACME Corp.");

        driver.findElement(By.className("trumbowyg-editor")).sendKeys("Very ugly duck");

        //filling in the fields on the tab 'Prices'
        driver.findElement(By.xpath("//*[@href = '#tab-prices']")).click();
        assertTrue(isElementPresent(By.xpath("//li[@class = 'active']/a[@href ='#tab-prices']")));

        String[][] tab_prices = {
                {"purchase_price", "33,33", "true"},
                {"gross_prices[USD]", "2,66", "true"},
                {"gross_prices[EUR]", "3,00", "true"}
        };

        setFields(tab_prices);

        setSelect("purchase_price_currency_code", "US Dollars");

        //save
        driver.findElement(By.name("save")).click();

        //check that product is added to all categories
        expandElements();
        List<WebElement> new_qty_products = driver.findElements(By.xpath(qty_products));
        assertTrue(current_qty_products.size() == new_qty_products.size() - 3);
    }

    public void expandElements(){
        //expand categories
        String rubber_ducks = "//a[text()= 'Rubber Ducks']";
        String subcategory = "//a[text()= 'Subcategory']";
        assertTrue(isElementPresent(By.xpath(rubber_ducks)));
        driver.findElement(By.xpath(rubber_ducks)).click();
        assertTrue(isElementPresent(By.xpath(subcategory)));
        driver.findElement(By.xpath(subcategory)).click();
    }

    public void setFields(String[][] array){
        for (int i=0;i < array.length;i++){
            WebElement element = driver.findElement(By.name(array[i][0]));
            if (array[i][2] == "true"){
                element.clear();
            }
            element.sendKeys(array[i][1]);
        }
    }

    public void setSelect(String name, String visibleText){

        WebElement select_field = driver.findElement(By.name(name));
        Select select = new Select(select_field);
        select.selectByVisibleText(visibleText);
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
