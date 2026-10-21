package com.poly.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegisterPage {
    private WebDriver driver;

    private By fullNameInput = By.id("fullName");
    private By usernameInput = By.id("reg-username");
    private By passwordInput = By.id("reg-password");
    private By registerBtn = By.id("btn-register");
    private By errorMessage = By.id("error-message");

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
    }

    public void register(String fullName, String username, String password) {
        driver.findElement(fullNameInput).clear();
        driver.findElement(fullNameInput).sendKeys(fullName);
        driver.findElement(usernameInput).clear();
        driver.findElement(usernameInput).sendKeys(username);
        driver.findElement(passwordInput).clear();
        driver.findElement(passwordInput).sendKeys(password);
        driver.findElement(registerBtn).click();
    }

    public String getErrorMessage() {
        return driver.findElement(errorMessage).getText();
    }
}