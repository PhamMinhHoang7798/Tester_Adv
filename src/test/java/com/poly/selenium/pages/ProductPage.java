package com.poly.selenium.pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProductPage {
    private WebDriver driver;

    private By addProductBtn = By.id("btn-add-product");
    private By logoutBtn = By.id("btn-logout");
    private By prodNameInput = By.id("prod-name");
    private By prodPriceInput = By.id("prod-price");
    private By prodQtyInput = By.id("prod-quantity");
    private By saveProductBtn = By.id("btn-save-product");

    public ProductPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickAddProduct() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(addProductBtn)).click();
    }

    public void enterProductForm(String name, String price, String quantity) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(prodNameInput)).clear();
        driver.findElement(prodNameInput).sendKeys(name);
        
        driver.findElement(prodPriceInput).clear();
        driver.findElement(prodPriceInput).sendKeys(price);
        
        driver.findElement(prodQtyInput).clear();
        driver.findElement(prodQtyInput).sendKeys(quantity);
        
        wait.until(ExpectedConditions.elementToBeClickable(saveProductBtn)).click();
    }

    public boolean isProductPresentInTable(String productName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        // Đợi URL điều hướng hoàn tất về lại /products
        wait.until(ExpectedConditions.urlContains("/products"));
        // Đợi cho đến khi DOM của bảng xuất hiện tên sản phẩm mới
        return wait.until(d -> d.getPageSource().contains(productName));
    }

    public void clickEditFirst() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//table[@id='product-table']//tbody//tr[1]//a[contains(@id,'edit-btn')]"))).click();
    }

    public void clickDeleteFirst() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//table[@id='product-table']//tbody//tr[1]//a[contains(@id,'delete-btn')]"))).click();
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert.accept();
    }

    public void clickLogout() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(logoutBtn)).click();
    }
}