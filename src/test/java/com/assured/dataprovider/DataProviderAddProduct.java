package com.assured.dataprovider;

import com.assured.constants.FrameworkConstants;
import com.assured.helpers.ExcelHelpers;
import com.assured.helpers.SystemHelpers;
import org.testng.annotations.DataProvider;

public class DataProviderAddProduct {
    @DataProvider(name = "data_provider_add_product")
    public Object[][] dataAddProduct() {
        ExcelHelpers excelHelpers = new ExcelHelpers();
        Object[][] data = excelHelpers.getDataHashTable(SystemHelpers.getCurrentDir() + FrameworkConstants.EXCEL_PROVIDER_DATA, "AddProduct", 2, 2);
        return data;
    }
}
