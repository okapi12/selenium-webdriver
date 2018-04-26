package ru.stqa.training.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static org.junit.Assert.assertTrue;


public class Task8withCorrections {
    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 5/*seconds*/);
    }

    @Test
    public void task8() {

        driver.get("http://localhost/litecart/en/");
        List<WebElement> elements = driver.findElements(By.xpath("//li[starts-with(@class, 'product')]/a[@class= 'link']"));
        System.out.println("Number of products on page: "+ elements.size());
        for (WebElement element: elements){
            List<WebElement> stickers = element.findElements(By.cssSelector("[class^=sticker]"));
            assertTrue(stickers.size() == 1);
        }
    }


    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
