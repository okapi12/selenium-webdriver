package ru.stqa.training.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class Task13withCorrections {

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 15/*seconds*/);
    }
    @Test
    public void task13withCorrections() {
        //adding 3 items
        for (int i=1;i <= 3;i++) {
            //initial page
            driver.get("http://localhost/litecart/en/");
            //search for the first item from the list and go to the product page
            driver.findElement(By.xpath("//li[starts-with(@class, 'product')]/a[@class= 'link']")).click();
            //check that the page has opened successfully
            assertTrue(isElementPresent(By.name("add_cart_product")));
            //Some products have a mandatory field "Size". Check its availability and fill if necessary
            List<WebElement> sizes = driver.findElements(By.name("options[Size]"));
            if (sizes.size() != 0){
                assertTrue(sizes.size() == 1);
                for (WebElement size: sizes){
                    Select s_size = new Select(size);
                    s_size.selectByIndex(1);
                }
            }
            //search cart counter
            WebElement counter_quantity = driver.findElement(By.xpath("//span[@class = 'quantity']"));
            //check cart counter value before adding product
            assertTrue(counter_quantity.getAttribute("textContent").equals(Integer.toString(i-1)));
            //adding a product to cart
            driver.findElement(By.name("add_cart_product")).click();
            //check that cart counter is updated
            wait.until(ExpectedConditions.attributeToBe(counter_quantity, "textContent", Integer.toString(i)));
        }
        //go to cart
        driver.findElement(By.xpath("//a[contains(@href, 'checkout')][@class = 'link']")).click();
        //check that cart is opened
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("remove_cart_item")));
        //find rows in table for products
        List<WebElement> prts = driver.findElements(By.xpath("//td[@class = 'item']"));
        //The page automatically switches between products before the first click on the product. Find the link to the product and click on it.
        driver.findElement(By.xpath("//a[@href = '#']")).click();
        //find links of all products in the cart
        List<WebElement> products = driver.findElements(By.xpath("//a[@href = '#']"));
        //remove items from the cart
        for (int i=0;i < products.size() - 1;i++) {
            List<WebElement> prd_check = driver.findElements(By.xpath("//a[@href = '#']"));
            prd_check.get(0).click();
            //check click on the link
            wait.until(ExpectedConditions.attributeToBe(prd_check.get(0),"className","inact act"));
            removeItem();
            //check that table with products is updated
            wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//td[@class = 'item']"), prts.size() - i - 1));
        }
        //delete the last item from the cart
        //check that the table with products is on the page
        WebElement product_table = driver.findElement((By.xpath("//*[contains(@class, 'dataTable')]")));
        removeItem();
        //check that the table is absent
        wait.until(ExpectedConditions.stalenessOf(product_table));
    }

    public void removeItem(){
        //getting all the buttons for removing the products
        List<WebElement> removes = driver.findElements(By.name("remove_cart_item"));
        //find visible button
        WebElement vis_remove = visible_element(removes);
        //remove item
        vis_remove.click();
    }

    public WebElement visible_element(List<WebElement> elements){
        List<WebElement> visible_elements = new ArrayList<>();
        for (WebElement element: elements){
            Boolean visible = element.isDisplayed();
            if (visible){
                visible_elements.add(element);
            }
        }
        //check that visible element is single
        assertTrue(visible_elements.size() == 1);
        return visible_elements.get(0);
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
