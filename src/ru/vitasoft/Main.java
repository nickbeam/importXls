package ru.vitasoft;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import ru.vitasoft.util.ExcelUtil;

import java.io.File;
import java.io.FileInputStream;

public class Main {

    public static void main(String[] args) {
        try {
            FileInputStream fis = new FileInputStream(new File("C:\\temp\\import.xls"));
            Workbook wb = new HSSFWorkbook(fis);
            Sheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object

            String dbTN = ExcelUtil.getDbTableName(sheet);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
