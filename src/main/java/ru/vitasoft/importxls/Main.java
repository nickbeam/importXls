package ru.vitasoft.importxls;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import ru.vitasoft.importxls.storage.SqlStorage;

import java.io.FileInputStream;
import java.util.Arrays;

import static ru.vitasoft.importxls.util.ArgsUtils.checkArgs;
import static ru.vitasoft.importxls.util.DateTimeUtils.getCurrentDateTime;
import static ru.vitasoft.importxls.util.ExcelUtils.*;

public class Main {
    private static final Logger log = LogManager.getLogger();

    public static void main(String[] args) {
        System.setProperty("log4j.configurationFile","src/main/resources/log4j2.xml");

        try {
            FileInputStream fis = new FileInputStream(checkArgs(args));
            Workbook wb = new HSSFWorkbook(fis);
            Sheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object
            log.log(Level.DEBUG, getCurrentDateTime() + " - Загрузка файла: " + Arrays.toString(args));
            SqlStorage sqlStorage = new SqlStorage(getDbUrl(sheet), getDbUser(), getDbPassword());
            sqlStorage.saveTableData(getDbTableName(), getFields(), getTableData());
            System.out.println("В таблицу: " + getDbTableName() + " загружено записей: " + getTableData().size());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
