package ru.stqa.training.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertTrue;

public class CartPage extends Page {

    public CartPage (WebDriver driver) {
        super(driver);
    }

    public CartPage stopSwitches(){
        driver.findElement(By.xpath("//a[@href = '#']")).click();
        return  this;
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
            System.out.println(visible);
            if (visible){
                visible_elements.add(element);
            }
        }
        //check that visible element is single
        assertTrue(visible_elements.size() == 1);
        return visible_elements.get(0);
    }

    public List<WebElement> findProductsInCart(){
        return driver.findElements(By.xpath("//a[@href = '#']"));
    }

    public CartPage selectProduct(){
        List<WebElement> prd_check = driver.findElements(By.xpath("//a[@href = '#']"));
        prd_check.get(0).click();
        wait.until(ExpectedConditions.attributeToBe(prd_check.get(0),"className","inact act"));
        return this;
    }

    public List<WebElement> findRowsInProductTable(){
        return driver.findElements(By.xpath("//td[@class = 'item']"));
    }

    public CartPage verifyTableUpdate(int size, int i){
       wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//td[@class = 'item']"), size - i - 1));
       return this;
    }

    public WebElement findTableOnThisPage() {
        return driver.findElement(By.xpath("//*[contains(@class, 'dataTable')]"));
    }

    public CartPage verifyThatTableIsAbsent(WebElement element){
        wait.until(ExpectedConditions.stalenessOf(element));
        return this;
    }


}
