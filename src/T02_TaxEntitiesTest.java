package com.assured.tests;

import com.assured.annotations.FrameworkAnnotation;
import com.assured.common.BaseTest;
import com.assured.enums.AuthorType;
import com.assured.enums.CategoryType;
import com.assured.pages.P02_TaxEntitiesPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

@Epic("Sanity Test")
@Feature("Tax Entities")
public class T02_TaxEntitiesTest extends BaseTest {


    P02_TaxEntitiesPage taxEntitiesPage;

    public T02_TaxEntitiesTest() {
        taxEntitiesPage = new P02_TaxEntitiesPage();

    }

    @FrameworkAnnotation(author = {AuthorType.Devisree}, category = {CategoryType.SANITY})
    @Test(priority = 1, description = "TC01_addTaxEntityWithValidCredentials")
    public void addTaxEntityWithValidCredentials() throws InterruptedException {
        taxEntitiesPage.loginWithValidCredentials();
        taxEntitiesPage.addTaxEntityWithValidCredentials();

    }

    @FrameworkAnnotation(author = {AuthorType.Devisree}, category = {CategoryType.SANITY})
    @Test(priority = 1, description = "TC02_addTaxEntityWithInValidCredentials")
    public void addTaxEntityWithInValidCredentials() throws InterruptedException {
        taxEntitiesPage.loginWithValidCredentials();
        taxEntitiesPage.addTaxEntityWithInValidCredentials();

    }

    @FrameworkAnnotation(author = {AuthorType.Devisree}, category = {CategoryType.SANITY})
    @Test(priority = 1, description = "TC03_addTaxEntityWithDuplicateCredentials")
    public void addTaxEntityWithDuplicateCredentials() throws InterruptedException {
        taxEntitiesPage.loginWithValidCredentials();
        taxEntitiesPage.addTaxEntityWithDuplicateCredentials();

    }

    @FrameworkAnnotation(author = {AuthorType.Devisree}, category = {CategoryType.SANITY})
    @Test(priority = 1, description = "TC04_addTaxEntityWithErrorMsg")
    public void addTaxEntityWithErrorMsg() throws InterruptedException {
        taxEntitiesPage.loginWithValidCredentials();
        taxEntitiesPage.addTaxEntityWithErrorMsg();

    }

    @FrameworkAnnotation(author = {AuthorType.Devisree}, category = {CategoryType.SANITY})
    @Test(priority = 1, description = "TC05_addTaxEntityWithRequiredFields")
    public void addTaxEntityWithRequiredFields() throws InterruptedException {
        taxEntitiesPage.loginWithValidCredentials();
        taxEntitiesPage.addTaxEntityWithRequiredFields();

    }

    @FrameworkAnnotation(author = {AuthorType.Devisree}, category = {CategoryType.SANITY})
    @Test(priority = 1, description = "TC06_addTaxEntityWithPercentageCompletion")
    public void addTaxEntityWithPercentageCompletion() throws InterruptedException {
        taxEntitiesPage.loginWithValidCredentials();
        taxEntitiesPage.addTaxEntityWithPercentageCompletion();

    }

    @FrameworkAnnotation(author = {AuthorType.Devisree}, category = {CategoryType.SANITY})
    @Test(priority = 1, description = "TC07_addTaxEntityWithDeleteTaxEntityProfile")
    public void addTaxEntityWithDeleteTaxEntityProfile() throws InterruptedException {
        taxEntitiesPage.loginWithValidCredentials();
        taxEntitiesPage.addTaxEntityWithDeleteTaxEntityProfile();

    }

}