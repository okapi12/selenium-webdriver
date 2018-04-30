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

public class Task11 {

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 5/*seconds*/);
    }

    @Test
    public void task11() {

        String password = RandomStringUtils.randomAlphanumeric(10);
        String email = RandomStringUtils.randomAlphanumeric(6)+ "@mail.ru";

        //values for elements: [name of field][value for field]
        String[][] web_elements = {
                {"tax_id", RandomStringUtils.randomNumeric(3)},
                {"company", "Company"},
                {"firstname", "Julia"},
                {"lastname", "Zorina"},
                {"address1", "St. Lenina 18"},
                {"address2", "St. Lebedeva 54"},
                {"postcode", RandomStringUtils.randomNumeric(5)},
                {"city", "Tomsk"},
                {"email", email},
                {"phone", RandomStringUtils.randomNumeric(11)},
                {"password", password },
                {"confirmed_password", password}
        };
        //initial page
        driver.get("http://localhost/litecart/en/");

        //go to registration page
        driver.findElement(By.xpath("//*[@id='box-account-login']//a[contains(@href,'create_account')]")).click();
        assertTrue(isElementPresent(By.xpath("//*[@id ='create-account']")));

        //filling fields with values
        for (int i=0;i < web_elements.length;i++){
            driver.findElement(By.name(web_elements[i][0])).sendKeys(web_elements[i][1]);
        }

        WebElement country_field = driver.findElement(By.name("country_code"));
        Select country_select = new Select(country_field);
        country_select.selectByVisibleText("United States");

        WebElement zone_code_field = driver.findElement(By.xpath("//select[@name = 'zone_code']"));
        Select zone_code_select = new Select(zone_code_field);
        zone_code_select.selectByVisibleText("Nevada");

        //account creation
        driver.findElement(By.name("create_account")).click();

        //first logout
        logout();

        //login
        assertTrue(isElementPresent(By.name("login")));
        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("login")).click();

        //second logout
        logout();
    }

    public void logout(){

        String locator = "//*[@id = 'box-account']//a[contains(@href, 'logout')]";
        assertTrue(isElementPresent(By.xpath(locator)));
        driver.findElement(By.xpath(locator)).click();

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
