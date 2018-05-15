package ru.stqa.training.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class ProductPage extends Page{

    public ProductPage (WebDriver driver) {
        super(driver);
    }

    public void setSize() {
        List<WebElement> sizes = driver.findElements(By.name("options[Size]"));
        if (sizes.size() != 0){
            assertTrue(sizes.size() == 1);
            for (WebElement size: sizes){
                Select s_size = new Select(size);
                s_size.selectByIndex(1);
            }
        }
    }

    public Boolean cartCounterIsGoodBeforeAddingProduct(int i){
        WebElement counter_quantity = driver.findElement(By.xpath("//span[@class = 'quantity']"));
        return counter_quantity.getAttribute("textContent").equals(Integer.toString(i-1));
    }

    public ProductPage addProduct() {
        driver.findElement(By.name("add_cart_product")).click();
        return  this;
    }

    public ProductPage waitUpdateCartCounter(int i){
        WebElement counter_quantity = driver.findElement(By.xpath("//span[@class = 'quantity']"));
        wait.until(ExpectedConditions.attributeToBe(counter_quantity, "textContent", Integer.toString(i)));
        return this;
    }

    public ProductPage goToCart(){
        driver.findElement(By.xpath("//a[contains(@href, 'checkout')][@class = 'link']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("remove_cart_item")));
        return  this;
    }

}
