package com.poly.selenium.tests;

import com.poly.selenium.pages.LoginPage;
import com.poly.selenium.pages.ProductPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ProductSeleniumTest extends BaseSeleniumTest {

    @Test(groups = {"smoke"}, priority = 1, description = "Them moi san pham")
    public void testCreateProduct() {
        driver.get(BASE_URL + "/login");
        new LoginPage(driver).login("hoang_tester", "Pass123456");

        ProductPage productPage = new ProductPage(driver);
        productPage.clickAddProduct();
        productPage.enterProductForm("Chuot Logitech G102", "399000", "20");

        Assert.assertTrue(productPage.isProductPresentInTable("Chuot Logitech G102"));
    }

    @Test(groups = {"smoke"}, priority = 2, description = "Cap nhat san pham")
    public void testUpdateProduct() {
        ProductPage productPage = new ProductPage(driver);
        productPage.clickEditFirst();
        productPage.enterProductForm("Chuot Logitech G102 Gen2", "450000", "30");

        Assert.assertTrue(productPage.isProductPresentInTable("Chuot Logitech G102 Gen2"));
    }

    @Test(groups = {"smoke"}, priority = 3, description = "Xoa san pham va dang xuat")
    public void testDeleteProductAndLogout() {
        ProductPage productPage = new ProductPage(driver);
        productPage.clickDeleteFirst();

        productPage.clickLogout();
        Assert.assertTrue(driver.getCurrentUrl().contains("/login"));
    }
}