package ru.stqa.training.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class Task19 {

    private WebDriver driver;
    private WebDriverWait wait;
    private InitialPage initialPage;
    private ProductPage productPage;
    private CartPage cartPage;

    @Before
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 15/*seconds*/);
        initialPage = new InitialPage(driver);
        productPage = new ProductPage(driver);
        cartPage = new CartPage(driver);
    }
    @Test
    public void task19() {

        //adding 3 items
        for (int i=1;i <= 3;i++) {
            //initial page
            initialPage.open();
            //search for the first item from the list and go to the product page
            initialPage.goToFirstProduct();
            //Some products have a mandatory field "Size". Check its availability and fill if necessary
            productPage.setSize();
            //check cart counter value before adding product
            assertTrue(productPage.cartCounterIsGoodBeforeAddingProduct(i));
            //adding a product to cart
            productPage.addProduct();
            //check that cart counter is updated
            productPage.waitUpdateCartCounter(i);
        }
        //go to cart
        productPage.goToCart();
        //find rows in table for products
        List<WebElement> prts = cartPage.findRowsInProductTable();
        //The page automatically switches between products before the first click on the product. Find the link to the product and click on it.
        cartPage.stopSwitches();
        //find links of all products in the cart
        List<WebElement> products = cartPage.findProductsInCart();
        //remove items from the cart
        for (int i=0;i < products.size() - 1;i++) {
            cartPage.selectProduct();
            cartPage.removeItem();
            //check that table with products is updated
            cartPage.verifyTableUpdate(prts.size(),i);
        }
        //delete the last item from the cart
        //check that the table with products is on the page
        WebElement product_table = cartPage.findTableOnThisPage();
        cartPage.removeItem();
        //check that the table is absent
        cartPage.verifyThatTableIsAbsent(product_table);
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
