package com.poly.selenium.tests;

import com.poly.selenium.pages.LoginPage;
import com.poly.selenium.pages.RegisterPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthSeleniumTest extends BaseSeleniumTest {

    @Test(groups = {"smoke"}, priority = 1, description = "Kiem tra dang ky tai khoan moi")
    public void testRegisterSuccess() {
        driver.get(BASE_URL + "/register");
        RegisterPage registerPage = new RegisterPage(driver);

        registerPage.register("Hoang Pham", "hoang_tester", "Pass123456");

        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.getSuccessMessage().contains("Đăng ký thành công"));
    }

    @Test(groups = {"smoke"}, priority = 2, description = "Kiem tra dang nhap sai mat khau")
    public void testLoginWrongPassword() {
        driver.get(BASE_URL + "/login");
        LoginPage loginPage = new LoginPage(driver);

        loginPage.login("hoang_tester", "wrong_pass");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Sai tên đăng nhập"));
    }

    @Test(groups = {"smoke"}, priority = 3, description = "Kiem tra dang nhap thanh cong")
    public void testLoginSuccess() {
        driver.get(BASE_URL + "/login");
        LoginPage loginPage = new LoginPage(driver);

        loginPage.login("hoang_tester", "Pass123456");
        Assert.assertTrue(driver.getCurrentUrl().contains("/products"));
    }
}