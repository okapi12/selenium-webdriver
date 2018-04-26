package ru.stqa.training.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.*;
//import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;


public class Task10Firefox {

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void start() {
      //  driver = new ChromeDriver();
        driver = new FirefoxDriver();
        wait = new WebDriverWait(driver, 5/*seconds*/);
    }

    @Test
    public void task10Firefox() {

        driver.get("http://localhost/litecart/en/");
        WebElement product_home = driver.findElement(By.xpath("//*[@id = 'box-campaigns']//li[starts-with(@class, 'product')]/a"));
        //get elements for general page
        String[] home_page = getElements(product_home, "name");
        //go to product page
        product_home.click();
        assertTrue(isElementPresent(By.id("box-product")));//check that the transfer is successful
        WebElement product_single = driver.findElement(By.xpath("//*[@id = 'box-product']"));
        //get elements for product page
        String[] product_page = getElements(product_single, "title");
        //check that name and prices are equals on general and product pages
        assertTrue(Arrays.equals(home_page, product_page));

    }

    public String[] getElements (WebElement element, String class_product_name){

        String array[] = new String[3];
        //get name
        WebElement p_name = element.findElement(By.className(class_product_name));
        String product_name = p_name.getAttribute("textContent");
        array[0] = product_name;

        //get regular price
        WebElement r_price = element.findElement(By.className("regular-price"));
        String regular_price = r_price.getAttribute("textContent");
        array[1] = regular_price;

        //get special price
        WebElement s_price = element.findElement(By.className("campaign-price"));
        String special_price = s_price.getAttribute("textContent");
        array[2] = special_price;

        //check that regular price is grey
        String color_rp = r_price.getCssValue("color");
        checkRGB(color_rp,"grey");

        //check that regular price is crossed out
        String strikethrough_text = r_price.getCssValue("text-decoration");
        assertTrue(strikethrough_text.indexOf("line-through") == 0);

        //check that special price is red
        String color_sp = s_price.getCssValue("color");
        checkRGB(color_sp,"red");

        //check that special price is bold
        String bold = s_price.getCssValue("font-weight");
        assertTrue(bold.equals("900") || bold.equals("700"));

        //check that regular price is less than special price
        int rp_size = r_price.getSize().width * r_price.getSize().height;
        int sp_size = s_price.getSize().width * s_price.getSize().height;
        assertTrue(rp_size < sp_size);

        return array;
    }

    public static void checkRGB(String color, String test){

        String[] numbers = color.replace("rgb(", "").replace(")", "").split(",");
        int r = Integer.parseInt(numbers[0].trim());
        int g = Integer.parseInt(numbers[1].trim());
        int b = Integer.parseInt(numbers[2].trim());
        if (test == "grey"){
            assertTrue(r == g && g == b);
        }
        else if (test == "red"){
            assertTrue(g == 0 && b == 0);
        }
        else {
            assertTrue(false);
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