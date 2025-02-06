package com.assured.pages;


public class CommonPageCRM {


    public P01_LoginPage loginPage;

    public P01_LoginPage getLoginPage() {
        if (loginPage == null) {
            loginPage = new P01_LoginPage();
        }
        return loginPage;
    }
}
