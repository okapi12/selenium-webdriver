package ru.stqa.training.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class InitialPage extends Page {

    public InitialPage (WebDriver driver) {
        super(driver);
    }
    public InitialPage open() {
        driver.get("http://localhost/litecart/en/");
        return this;
    }

    public InitialPage goToFirstProduct() {
        driver.findElement(By.xpath("//li[starts-with(@class, 'product')]/a[@class= 'link']")).click();
        wait.until((WebDriver d) -> d.findElement(By.name("add_cart_product")));
        return this;
    }

}
