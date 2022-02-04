package ru.vitasoft;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import ru.vitasoft.storage.SqlStorage;

import java.io.FileInputStream;

import static ru.vitasoft.util.ArgsUtils.checkArgs;
import static ru.vitasoft.util.ExcelUtils.*;

public class Main {

    public static void main(String[] args) {

        try {
            FileInputStream fis = new FileInputStream(checkArgs(args));
            Workbook wb = new HSSFWorkbook(fis);
            Sheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object

            SqlStorage sqlStorage = new SqlStorage(getDbUrl(sheet), getDbUser(), getDbPassword());
            sqlStorage.saveTableData(getDbTableName(), getTableData());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
