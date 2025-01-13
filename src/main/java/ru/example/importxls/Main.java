package ru.example.importxls;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import ru.example.importxls.storage.SqlStorage;
import ru.example.importxls.util.ArgsUtils;
import ru.example.importxls.util.DateTimeUtils;
import ru.example.importxls.util.ExcelUtils;

import java.io.FileInputStream;
import java.util.Arrays;

public class Main {
    private static final Logger log = LogManager.getLogger();

    public static void main(String[] args) {
        System.setProperty("log4j.configurationFile","src/main/resources/log4j2.xml");

        try {
            FileInputStream fis = new FileInputStream(ArgsUtils.checkArgs(args));
            Workbook wb = new HSSFWorkbook(fis);
            Sheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object
            log.log(Level.DEBUG, DateTimeUtils.getCurrentDateTime() + " - Загрузка файла: " + Arrays.toString(args));
            SqlStorage sqlStorage = new SqlStorage(ExcelUtils.getDbUrl(sheet), ExcelUtils.getDbUser(), ExcelUtils.getDbPassword());
            int insertedRows = sqlStorage.saveTableData(ExcelUtils.getDbTableName(), ExcelUtils.getFields(), ExcelUtils.getTableData());
            System.out.println("В таблицу: " + ExcelUtils.getDbTableName() + " загружено записей: " + insertedRows);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
