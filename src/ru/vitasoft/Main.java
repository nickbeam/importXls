package ru.vitasoft;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import ru.vitasoft.model.Field;
import ru.vitasoft.util.ExcelUtils;

import java.io.FileInputStream;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            FileInputStream fis = new FileInputStream("C:\\temp\\import.xls");
            Workbook wb = new HSSFWorkbook(fis);
            Sheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object

            String dbTN = ExcelUtils.getDbTableName(sheet);
            List<Field> fields = ExcelUtils.getFields(sheet);
            List<List<String>> tableData = ExcelUtils.getTableData(sheet);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
