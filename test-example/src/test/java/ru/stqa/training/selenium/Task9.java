package ru.stqa.training.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.Arrays;


import java.util.List;

public class Task9 {
    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 5/*seconds*/);

        //login
        driver.get("http://localhost/litecart/admin/");
        WebElement username = driver.findElement(By.name("username"));
        username.sendKeys("admin");
        WebElement password = driver.findElement(By.name("password"));
        password.sendKeys("admin");
        driver.findElement(By.name("remember_me")).click();
        driver.findElement(By.name("login")).click();
        assertTrue(isElementPresent(By.id("box-apps-menu")));//check that the login was successful
    }

    @Test
    public void task9_1() {

        task9("http://localhost/litecart/admin/?app=countries&doc=countries", 5, 6, "");

    }

    @Test
    public void task9_2() {

        task9("http://localhost/litecart/admin/?app=geo_zones&doc=geo_zones", 3, 4, "//option[@selected='selected']");

    }

    public void task9(String link, int row_name_id, int row_zone_id, String option){

        driver.get(link);
        WebElement dataTable = driver.findElement(By.className("dataTable"));
        List<WebElement> rows = dataTable.findElements(By.className("row"));
        System.out.println("Number of rows in table with countries: " + rows.size());

        String names[] = new String[rows.size()];
        //retrieving values for countries and zones
        for (int i = 1; i <= rows.size(); i++) {
            WebElement row_name = driver.findElement(By.xpath("//*[@class = 'row'][" + i + "]/td["+row_name_id+"]/a"));
            String name = row_name.getAttribute("innerText");
            names[i - 1] = name;
            System.out.println("Country " +i+ ": " + name);
            WebElement row_zones = driver.findElement(By.xpath("//*[@class = 'row'][" + i + "]/td["+row_zone_id+"]"));
            int number_zones = Integer.parseInt(row_zones.getAttribute("innerText"));

            if (number_zones != 0) {
                String name_zones[] = new String[number_zones];
                row_name.click();
                assertTrue(isElementPresent(By.id("table-zones")));
                for (int i1 = 2; i1 <= number_zones + 1; i1++) {
                    WebElement zone_name = driver.findElement(By.xpath("//*[@id='table-zones']//tr[" + i1 + "]/td[3]"+ option));
                    String zone = zone_name.getAttribute("textContent");
                    name_zones[i1 - 2] = zone;
                    System.out.println("Zone " + (i1-1) + ": " + zone);
                }
                assertTrue(isSortedArray(name_zones)); //check that the zones are in alphabetical order
            }
            driver.get(link);

        }
        assertTrue(isSortedArray(names));//check that the countries are in alphabetical order
    }

    public boolean isSortedArray(String[] array) {

            String[] array_copy = array.clone(); //copying an array
            Arrays.sort(array_copy);//sorting in alphabetical order
            return Arrays.equals(array,array_copy);//arrays comparison
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
