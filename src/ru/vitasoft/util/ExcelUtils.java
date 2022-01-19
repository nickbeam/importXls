package ru.vitasoft.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import ru.vitasoft.model.Field;

import java.util.*;

import static org.apache.poi.ss.usermodel.CellType.*;

public class ExcelUtils {
    public static String getDbTableName(Sheet sheet) {
        if (sheet.getRow(2).getCell(0).getCellType() != STRING) {
            System.out.println("DB table name is empty, check xls file");
            System.exit(1);
        }
        return sheet.getRow(2).getCell(0).getStringCellValue();
    }

    public static List<Field> getFields(Sheet sheet) {
        List<Field> fields = new ArrayList<>();

        for (int curCol = 1; curCol <= getColCount(sheet); curCol++) {
            fields.add(new Field(
                    getCellStringData(sheet, curCol, 2),    //name
                    getCellStringData(sheet, curCol, 3),    //defValue
                    getCellStringData(sheet, curCol, 5),    //type
                    false,    //uniq
                    curCol    //colNumber
            ));
        }
        return fields;
    }

    public static String getCellStringData(Sheet sheet, int colNum, int rowNum) {
        Row row = sheet.getRow(rowNum);
        Cell cell = row.getCell(colNum);
        String cellData = "";
        try {
            cellData = cell.getStringCellValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cellData;
    }

    public static String getCellStringData(Cell cell) {
        String cellData = "";
        try {
            switch (cell.getCellType()) {
                case STRING:
                    cellData = cell.getStringCellValue();
                    break;
                case NUMERIC:
                    System.out.println("NUMERIC");
                    break;
                case BOOLEAN:
                    System.out.println("BOOLEAN");
                    break;
                case FORMULA:
                    System.out.println("FORMULA");
                    break;
                default:
                    System.out.println("default");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cellData;
    }

    public static List<List<String>> getTableData(Sheet sheet) {
        List<List<String>> tableData = new ArrayList<>();
        for (int curRow = 8; curRow <= getRowCount(sheet); curRow++) {
            tableData.add(getRowData(sheet.getRow(curRow)));
        }
        return tableData;
    }

    public static List<String> getRowData(Row row) {
        List<String> rowData = new ArrayList<>();
        for (Cell cell : row) {
            if (cell.getColumnIndex() == 0) {   //Skip first column cell data
                continue;
            }
            cell.setCellType(STRING);
            rowData.add(getCellStringData(cell));
        }
        return rowData;
    }

    public static int getColCount(Sheet sheet) {
        return sheet.getRow(2).getLastCellNum();
    }

    public static int getRowCount(Sheet sheet) {
        return sheet.getLastRowNum();
    }
}
