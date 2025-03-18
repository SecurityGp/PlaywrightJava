package com.assured.pages;

import com.assured.constants.FrameworkConstants;
import com.assured.helpers.SystemHelpers;
import com.assured.utils.DataGenerateUtils;
import com.microsoft.playwright.options.AriaRole;

import static com.assured.services.PageActions.*;
import static java.lang.Thread.sleep;

public class P02_TaxEntitiesPage {

    public static final String tfInputEmailSelector = "#normal_login_email";
    public static final String tfInputPasswordSelector = "#normal_login_password";
    public static final String tfLegalEntityNameSelector = "#entityInfoForm_legalName";
    public static final String tfType2NPISelector = "#entityInfoForm_npi";
    public static final String tfTaxIDSelector = "#entityInfoForm_taxId";
    public static final String tfContactTitleSelector = "#entityInfoForm_contactTitle";
    public static final String tfContactNameSelector = "#entityInfoForm_contactName";
    public static final String tfContactEmailSelector = "#entityInfoForm_email";
    public static final String tfContactPhoneSelector = "#entityInfoForm_phoneNumber";
    public static final String tfContactFaxSelector = "#entityInfoForm_faxNumber";
    public static final String ltClickEntityNameSelector = "//td[@class='ant-table-cell']/a[contains(@href,'/admin/tax-entities/profiles')]";
    public static final String ltFirstEntityNameSelector = "tbody tr:nth-child(1) td:nth-child(1) a";
    public static final String tfStreetAddress1Selector = "#addressForm_mailingStreetAddress1";
    public static final String tfMailingCitySelector = "#addressForm_mailingCity";
    public static final String ddMailingStateSelector = "#addressForm_mailingState";
    public static final String tfMailingZipCodeSelector = "#addressForm_mailingZipCode";
    public static final String cbTelehealthSelector = "//span[contains(@class,'ant-checkbox ant-wave-target')]//input[@value='telehealth']";
    public static final String cbInPersonSelector = "//span[contains(@class,'ant-checkbox ant-wave-target')]//input[@value='in_person']";
    public static final String upEntityW9Selector = "//div[@id='documentsForm_entityW9']//button[@type='button']";
    public static final String upCP575IRSLetterSelector = "//div[@id='documentsForm_cp575IRSLetter']//button[@type='button']";
    public static final String upCMSDisclosureSelector = "//div[@id='documentsForm_cmsDisclosure']//button[@type='button']";
    public static final String upLiabilityInsuranceSelector = "//div[@id='documentsForm_liabilityInsurance']//button[@type='button']";
    public static final String upFictiousNamePermitSelector = "//div[@id='documentsForm_fictitiousNamePermit']//span[contains(text(),'Upload')]";
    public static final String profileCompletionSelector = "span[class='ant-progress-text']";    //TODO: Check if this is still valid
    public static final String ltSummarySelector = "//div[@data-node-key='summary']/div[contains(@id,'tab-summary')]";

    public P02_TaxEntitiesPage() {
        super();

    }

    String legalName = DataGenerateUtils.legalEntityName() + DataGenerateUtils.randomString(4);
    String streetAddress = DataGenerateUtils.randomStreetAddress1();
    String firstName = DataGenerateUtils.randomFirstName();
    String city = DataGenerateUtils.randomCity();
    String zipCode = DataGenerateUtils.randomZipCode();
    String npi = DataGenerateUtils.randomNpiNumber();
    String taxID = DataGenerateUtils.randomTaxId();
    String accountNumber = DataGenerateUtils.randomAccountNumber();
    String routingNumber = DataGenerateUtils.randomRoutingNumber();
    String contactEmail = DataGenerateUtils.randomString(3) + "@gp.com";
    String contactPhone = DataGenerateUtils.randomPhoneNumber();
    String faxNumber = DataGenerateUtils.randomFaxNumber();
//    final String dataFilePath = SystemHelpers.getCurrentDir() + "src\\test\\resources\\testdata\\Common\\docRef.pdf";
final String dataFilePath = Paths.get(SystemHelpers.getCurrentDir(),
        "src", "test", "resources", "testdata", "Common", "docRef.pdf").toString();
    String verifyTextUpdatedMsg="updated";

    String verifyTextAddedMsg="added";
    String verifyTextSavedMsg="saved";

    public P02_TaxEntitiesPage loginWithValidCredentials() throws InterruptedException {

        navigate(FrameworkConstants.URL_STAGING);
        String email = "devishree.raja+11@iopex.com";
        String password = "Iopex@2025";
        setText(tfInputEmailSelector, email);
        setText(tfInputPasswordSelector, password);
        clickElementByRole(AriaRole.BUTTON, "Log in", false);
        return new P02_TaxEntitiesPage();
    }

    public P02_TaxEntitiesPage addTaxEntityWithValidCredentials() throws InterruptedException {
        String legalName1 = legalName;
        clickElementByRole(AriaRole.LINK, "Tax Entities", false);
        clickElementByRole(AriaRole.BUTTON, "plus-square Add Tax Entity", false);
        clickElement(tfLegalEntityNameSelector);
        setText(tfLegalEntityNameSelector, legalName1);
        clickElementByLabel("State of Incorporation");
        clickElementByText("AK - Alaska");
        clickElement(tfType2NPISelector);
        setText(tfType2NPISelector, npi);
        clickElement(tfTaxIDSelector);
        setText(tfTaxIDSelector, taxID);
        clickElement(tfContactTitleSelector);
        setText(tfContactTitleSelector, "Manager");
        clickElement(tfContactNameSelector);
        setText(tfContactNameSelector, firstName);
        clickElement(tfContactEmailSelector);
        setText(tfContactEmailSelector, contactEmail);
        clickElement(tfContactPhoneSelector);
        setText(tfContactPhoneSelector, contactPhone);
        clickElement(tfContactFaxSelector);
        setText(tfContactFaxSelector, faxNumber);
        clickElementByRole(AriaRole.BUTTON, "Save", false);
        sleep(2000);
        verifyElementTextContains("body", verifyTextAddedMsg);

        clickElementByPlaceholder("Search by Legal Name, TIN, NPI");
        fillElementByPlaceholder("Search by Legal Name, TIN, NPI", legalName1);
        sleep(3000);
        clickElement(ltClickEntityNameSelector);
        clickElementByRole(AriaRole.TAB, "Entity Info", false);
        clickElementByRole(AriaRole.BUTTON, "Save", false);
        sleep(2000);
        verifyElementTextContains("body", verifyTextUpdatedMsg);

        clickElementByRole(AriaRole.TAB, "Address", false);
        clickElement(tfStreetAddress1Selector);
        setText(tfStreetAddress1Selector, streetAddress);
        clickElement(tfMailingCitySelector);
        setText(tfMailingCitySelector, city);
        clickElement(ddMailingStateSelector);
        clickElementByText("CA - California");
        clickElement(tfMailingZipCodeSelector);
        setText(tfMailingZipCodeSelector, zipCode);
        checkElementByLabel("Same as Mailing Address");
        clickElementByRole(AriaRole.BUTTON, "Save", false);
        sleep(2000);
        verifyElementTextContains("body", verifyTextUpdatedMsg);

        clickElementByRole(AriaRole.TAB, "Financial Info", false);
        clickElementByLabel("Account Number");
        fillElementByLabel("Account Number", accountNumber);
        clickElementByLabel("Routing Number");
        fillElementByLabel("Routing Number", routingNumber);
        uploadFiles("input[type='file']","button:has-text('Upload')", dataFilePath);
        clickElementByRole(AriaRole.BUTTON, "Save", false);
        sleep(2000);
        verifyElementTextContains("body", verifyTextUpdatedMsg);

        clickElementByRole(AriaRole.TAB, "Operational Info", false);
        clickElement(cbTelehealthSelector);
        clickElement(cbInPersonSelector);
        clickElementByRole(AriaRole.BUTTON, "Save", false);
        sleep(2000);
        verifyElementTextContains("body", verifyTextUpdatedMsg);

        clickElementByRole(AriaRole.TAB, "Documents", false);
        uploadFiles("#documentsForm_entityW9 input[name=\"file\"]",upEntityW9Selector, dataFilePath);
        sleep(2000);
        uploadFiles("#documentsForm_cp575IRSLetter input[name=\"file\"]",upCP575IRSLetterSelector, dataFilePath);
        sleep(2000);
        uploadFiles("#documentsForm_cmsDisclosure input[name=\"file\"]",upCMSDisclosureSelector, dataFilePath);
        sleep(2000);
        uploadFiles("#documentsForm_liabilityInsurance input[name=\"file\"]",upLiabilityInsuranceSelector, dataFilePath);
        sleep(2000);
        uploadFiles("#documentsForm_fictitiousNamePermit input[name=\"file\"]",upFictiousNamePermitSelector, dataFilePath);
        sleep(8000);
        clickElementByRole(AriaRole.BUTTON, "Save", false);
        sleep(2000);
        verifyElementTextContains("body", verifyTextSavedMsg);

        return new P02_TaxEntitiesPage();
    }

    String legalName2 = DataGenerateUtils.legalEntityName() + DataGenerateUtils.randomString(3);

    public P02_TaxEntitiesPage addTaxEntityWithInValidCredentials() throws InterruptedException {

        String legalName1 = legalName2;
        clickElementByRole(AriaRole.LINK, "Tax Entities", false);
        clickElementByRole(AriaRole.BUTTON, "plus-square Add Tax Entity", false);
        clickElement(tfLegalEntityNameSelector);
        setText(tfLegalEntityNameSelector, legalName1);
        clickElementByLabel("State of Incorporation");
        clickElementByText("AK - Alaska");
        clickElement(tfType2NPISelector);
        setText(tfType2NPISelector, "234");
        clickElement(tfTaxIDSelector);
        setText(tfTaxIDSelector, taxID);
        clickElement(tfContactTitleSelector);
        setText(tfContactTitleSelector, "Manager");
        clickElement(tfContactNameSelector);
        setText(tfContactNameSelector, firstName);
        clickElement(tfContactEmailSelector);
        setText(tfContactEmailSelector, contactEmail);
        clickElement(tfContactPhoneSelector);
        setText(tfContactPhoneSelector, contactPhone);
        clickElement(tfContactFaxSelector);
        setText(tfContactFaxSelector, faxNumber);
        clickElementByRole(AriaRole.BUTTON, "Save", false);
        sleep(2000);
        verifyElementTextContains("body", "must be");

        return new P02_TaxEntitiesPage();
    }

    public P02_TaxEntitiesPage addTaxEntityWithDuplicateCredentials() throws InterruptedException {

        clickElementByRole(AriaRole.LINK, "Tax Entities", false);
        final String firstLegalEntityName = getTextContent(ltFirstEntityNameSelector);
        clickElementByRole(AriaRole.BUTTON, "plus-square Add Tax Entity", false);
        clickElement(tfLegalEntityNameSelector);
        setText(tfLegalEntityNameSelector, firstLegalEntityName);
        clickElementByLabel("State of Incorporation");
        clickElementByText("AK - Alaska");
        clickElement(tfType2NPISelector);
        setText(tfType2NPISelector, npi);
        clickElement(tfTaxIDSelector);
        setText(tfTaxIDSelector, taxID);
        clickElement(tfContactTitleSelector);
        setText(tfContactTitleSelector, "Manager");
        clickElement(tfContactNameSelector);
        setText(tfContactNameSelector, firstName);
        clickElement(tfContactEmailSelector);
        setText(tfContactEmailSelector, contactEmail);
        clickElement(tfContactPhoneSelector);
        setText(tfContactPhoneSelector, contactPhone);
        clickElement(tfContactFaxSelector);
        setText(tfContactFaxSelector, faxNumber);
        sleep(1000);
        clickElementByRole(AriaRole.BUTTON, "Save", false);
        sleep(2000);
        verifyElementTextContains("body", "already exists");
        return new P02_TaxEntitiesPage();
    }

    public P02_TaxEntitiesPage addTaxEntityWithErrorMsg() throws InterruptedException {

        clickElementByRole(AriaRole.LINK, "Tax Entities", false);
        clickElementByRole(AriaRole.BUTTON, "plus-square Add Tax Entity", false);
        clickElement(tfLegalEntityNameSelector);
        setText(tfLegalEntityNameSelector, " ");
        clickElementByLabel("State of Incorporation");
        clickElementByText("AK - Alaska");
        clickElement(tfType2NPISelector);
        setText(tfType2NPISelector, npi);
        clickElement(tfTaxIDSelector);
        setText(tfTaxIDSelector, taxID);
        clickElement(tfContactTitleSelector);
        setText(tfContactTitleSelector, "Manager");
        clickElement(tfContactNameSelector);
        setText(tfContactNameSelector, firstName);
        clickElement(tfContactEmailSelector);
        setText(tfContactEmailSelector, contactEmail);
        clickElement(tfContactPhoneSelector);
        setText(tfContactPhoneSelector, contactPhone);
        clickElement(tfContactFaxSelector);
        setText(tfContactFaxSelector, faxNumber);
        clickElementByRole(AriaRole.BUTTON, "Save", false);
        sleep(2000);
        verifyElementTextContains("body", "not be");

        return new P02_TaxEntitiesPage();
    }

    String legalName4 = DataGenerateUtils.legalEntityName() + DataGenerateUtils.randomString(3);

    public P02_TaxEntitiesPage addTaxEntityWithRequiredFields() throws InterruptedException {
        String legalName1 = legalName4;
        clickElementByRole(AriaRole.LINK, "Tax Entities", false);
        clickElementByRole(AriaRole.BUTTON, "plus-square Add Tax Entity", false);
        clickElement(tfLegalEntityNameSelector);
        setText(tfLegalEntityNameSelector, legalName1);
        clickElementByLabel("State of Incorporation");
        clickElementByText("AK - Alaska");
        clickElement(tfType2NPISelector);
        setText(tfType2NPISelector, "");
        clickElement(tfTaxIDSelector);
        setText(tfTaxIDSelector, taxID);
        clickElement(tfContactTitleSelector);
        setText(tfContactTitleSelector, "Manager");
        clickElement(tfContactNameSelector);
        setText(tfContactNameSelector, firstName);
        clickElement(tfContactEmailSelector);
        setText(tfContactEmailSelector, contactEmail);
        clickElement(tfContactPhoneSelector);
        setText(tfContactPhoneSelector, contactPhone);
        clickElement(tfContactFaxSelector);
        setText(tfContactFaxSelector, faxNumber);
        clickElementByRole(AriaRole.BUTTON, "Save", false);
        sleep(2000);
        verifyElementTextContains("body", "required");

        return new P02_TaxEntitiesPage();
    }

    String legalName5 = DataGenerateUtils.legalEntityName() + DataGenerateUtils.randomString(3);

    public P02_TaxEntitiesPage addTaxEntityWithPercentageCompletion() throws InterruptedException {

        String legalName1 = legalName5;
        clickElementByRole(AriaRole.LINK, "Tax Entities", false);
        clickElementByRole(AriaRole.BUTTON, "plus-square Add Tax Entity", false);
        clickElement(tfLegalEntityNameSelector);
        setText(tfLegalEntityNameSelector, legalName1);
        clickElementByLabel("State of Incorporation");
        clickElementByText("AK - Alaska");
        clickElement(tfType2NPISelector);
        setText(tfType2NPISelector, npi);
        clickElement(tfTaxIDSelector);
        setText(tfTaxIDSelector, taxID);
        clickElement(tfContactTitleSelector);
        setText(tfContactTitleSelector, "Manager");
        clickElement(tfContactNameSelector);
        setText(tfContactNameSelector, firstName);
        clickElement(tfContactEmailSelector);
        setText(tfContactEmailSelector, contactEmail);
        clickElement(tfContactPhoneSelector);
        setText(tfContactPhoneSelector, contactPhone);
        clickElement(tfContactFaxSelector);
        setText(tfContactFaxSelector, faxNumber);
        clickElementByRole(AriaRole.BUTTON, "Save", false);
        sleep(2000);
        verifyElementTextContains("body", verifyTextAddedMsg);

        clickElementByPlaceholder("Search by Legal Name, TIN, NPI");
        fillElementByPlaceholder("Search by Legal Name, TIN, NPI", legalName1);
        sleep(3000);
        clickElement(ltClickEntityNameSelector);
        sleep(2000);
        verifyElementTextContains(profileCompletionSelector, "20%");

        clickElementByRole(AriaRole.TAB, "Entity Info", false);
        clickElementByRole(AriaRole.BUTTON, "Save", false);
        sleep(2000);
        verifyElementTextContains("body", verifyTextUpdatedMsg);

        clickElementByRole(AriaRole.TAB, "Address", false);
        clickElement(tfStreetAddress1Selector);
        setText(tfStreetAddress1Selector, streetAddress);
        clickElement(tfMailingCitySelector);
        setText(tfMailingCitySelector, city);
        clickElement(ddMailingStateSelector);
        clickElementByText("CA - California");
        clickElement(tfMailingZipCodeSelector);
        setText(tfMailingZipCodeSelector, zipCode);
        checkElementByLabel("Same as Mailing Address");
        clickElementByRole(AriaRole.BUTTON, "Save", false);
        sleep(2000);
        verifyElementTextContains("body", verifyTextUpdatedMsg);
        sleep(2000);
        clickElement(ltSummarySelector);
        sleep(4000);
        verifyElementTextContains(profileCompletionSelector, "55%");

        clickElementByRole(AriaRole.TAB, "Financial Info", false);
        clickElementByLabel("Account Number");
        fillElementByLabel("Account Number", accountNumber);
        clickElementByLabel("Routing Number");
        fillElementByLabel("Routing Number", routingNumber);
        clickElementByRole(AriaRole.BUTTON, "Save", false);
        sleep(4000);
        verifyElementTextContains("body", verifyTextUpdatedMsg);

        clickElement(ltSummarySelector);
        sleep(4000);
        verifyElementTextContains(profileCompletionSelector, "65%");
        clickElementByRole(AriaRole.TAB, "Operational Info", false);
        clickElement(cbTelehealthSelector);
        clickElement(cbInPersonSelector);
        clickElementByRole(AriaRole.BUTTON, "Save", false);
        sleep(2000);
        verifyElementTextContains("body", verifyTextUpdatedMsg);

        clickElement(ltSummarySelector);
        sleep(2000);
        verifyElementTextContains(profileCompletionSelector, "70%");
        clickElementByRole(AriaRole.TAB, "Documents", false);
        uploadFile(upEntityW9Selector, dataFilePath);
        sleep(2000);
        uploadFile(upCP575IRSLetterSelector, dataFilePath);
        sleep(2000);
        uploadFile(upCMSDisclosureSelector, dataFilePath);
        sleep(2000);
        uploadFile(upLiabilityInsuranceSelector, dataFilePath);
        sleep(2000);
        uploadFile(upFictiousNamePermitSelector, dataFilePath);
        sleep(8000);
        clickElementByRole(AriaRole.BUTTON, "Save", false);
        sleep(2000);
        verifyElementTextContains("body", verifyTextSavedMsg);
        clickElement(ltSummarySelector);
        sleep(2000);
        verifyElementTextContains(profileCompletionSelector, "90%");

        return new P02_TaxEntitiesPage();
    }

    String legalName6 = DataGenerateUtils.legalEntityName() + DataGenerateUtils.randomString(3);

    public P02_TaxEntitiesPage addTaxEntityWithDeleteTaxEntityProfile() throws InterruptedException {
        String legalName1 = legalName6;
        clickElementByRole(AriaRole.LINK, "Tax Entities", false);
        clickElementByRole(AriaRole.BUTTON, "plus-square Add Tax Entity", false);
        clickElement(tfLegalEntityNameSelector);
        setText(tfLegalEntityNameSelector, legalName1);
        clickElementByLabel("State of Incorporation");
        clickElementByText("AK - Alaska");
        clickElement(tfType2NPISelector);
        setText(tfType2NPISelector, npi);
        clickElement(tfTaxIDSelector);
        setText(tfTaxIDSelector, taxID);
        clickElement(tfContactTitleSelector);
        setText(tfContactTitleSelector, "Manager");
        clickElement(tfContactNameSelector);
        setText(tfContactNameSelector, firstName);
        clickElement(tfContactEmailSelector);
        setText(tfContactEmailSelector, contactEmail);
        clickElement(tfContactPhoneSelector);
        setText(tfContactPhoneSelector, contactPhone);
        clickElement(tfContactFaxSelector);
        setText(tfContactFaxSelector, faxNumber);
        clickElementByRole(AriaRole.BUTTON, "Save", false);
        sleep(2000);
        verifyElementTextContains("body", verifyTextAddedMsg);

        clickElementByPlaceholder("Search by Legal Name, TIN, NPI");
        fillElementByPlaceholder("Search by Legal Name, TIN, NPI", legalName1);
        sleep(3000);
        clickElement(ltClickEntityNameSelector);
        clickElementByRole(AriaRole.BUTTON, "Delete Tax Entity Profile", false);
        clickElementByPlaceholder("Entity name");
        fillElementByPlaceholder("Entity name", legalName1);
        clickElementByRole(AriaRole.BUTTON, "Yes, Delete", false);
        sleep(2000);
        verifyElementTextContains("body", "deactivated");


        return new P02_TaxEntitiesPage();
    }
}