package ru.vitasoft.importxls;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import ru.vitasoft.importxls.storage.SqlStorage;

import java.io.FileInputStream;

import static ru.vitasoft.importxls.util.ArgsUtils.checkArgs;
import static ru.vitasoft.importxls.util.ExcelUtils.*;

public class Main {

    public static void main(String[] args) {

        try {
            FileInputStream fis = new FileInputStream(checkArgs(args));
            Workbook wb = new HSSFWorkbook(fis);
            Sheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object

            SqlStorage sqlStorage = new SqlStorage(getDbUrl(sheet), getDbUser(), getDbPassword());
            sqlStorage.saveTableData(getDbTableName(), getTableData());
            System.out.println("В таблицу: " + getDbTableName() + " загружено записей: " + getTableData().size());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
