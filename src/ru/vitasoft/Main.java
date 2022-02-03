package ru.vitasoft;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import ru.vitasoft.model.Field;
import ru.vitasoft.storage.SqlStorage;

import java.io.FileInputStream;
import java.util.List;

import static ru.vitasoft.util.ExcelUtils.*;

public class Main {

    public static void main(String[] args) {
        try {
            FileInputStream fis = new FileInputStream("C:\\temp\\import.xls");
            Workbook wb = new HSSFWorkbook(fis);
            Sheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object

            setSheet(sheet);
//            String dbUrl = getDbUrl();
//            String dbUser = getDbUser();
//            String dbPassword = getDbPassword();
//            String dbTN = getDbTableName();
            List<Field> fields = getFields();
//            String fieldsStr = getFieldsStr();
//            String fieldsParam = getParamsStr();
//            List<List<String>> tableData = getTableData();
            SqlStorage sqlStorage = new SqlStorage(getDbUrl(), getDbUser(), getDbPassword());
            sqlStorage.saveTableData(getDbTableName(), getTableData());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
