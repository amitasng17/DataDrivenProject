package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

public class ExcelDataProvider {

	@DataProvider(name = "excelData")
    public Object[][] excelDataProvider(ITestContext context) throws IOException {
		   
           String testName=context.getCurrentXmlTest().getName();
           Object[][] arrObj = getExcelData("C:\\D Drive\\Test Automation\\FinalDataDrivenwithTestNG\\src\\test\\resources\\testdata.xlsx",testName);
        return arrObj;
}
 
   
    public Object[][] getExcelData(String fileName, String sheetName) throws IOException {
    	Object[][] data = null;
        try {
 
            FileInputStream fis = new FileInputStream(fileName);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheet(sheetName);
            XSSFRow row = sheet.getRow(0);
            int noOfRows = sheet.getPhysicalNumberOfRows();
            int noOfCols = row.getLastCellNum();
            Cell cell;
            data = new Object[noOfRows - 1][noOfCols];
 
            for (int i = 1; i < noOfRows; i++) {
                for (int j = 0; j < noOfCols; j++) {
                    row = sheet.getRow(i);
                    cell = row.getCell(j);
                    
                    //data[i - 1][j] = cell.getStringCellValue();
                   switch(cell.getCellType())	{
                
					case 1:
						data[i - 1][j] = (cell.getStringCellValue());
						break;
					case 0:
						if (DateUtil.isCellDateFormatted(cell)) {
							Date date = cell.getDateCellValue();
							SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
							String strDate = dateFormat.format(date);
							data[i - 1][j] = strDate;
							break;
						} else {
							data[i - 1][j] = (int) (cell.getNumericCellValue());
							break;
						}

					case 4:
						data[i - 1][j] = (cell.getBooleanCellValue());
						break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("The exception is: " + e.getMessage());
        }
        return data;
    }
}
