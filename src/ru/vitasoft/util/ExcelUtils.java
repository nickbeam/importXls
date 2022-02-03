package ru.vitasoft.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import ru.vitasoft.model.Field;

import java.util.*;

import static org.apache.poi.ss.usermodel.CellType.*;

public class ExcelUtils {
    private static String dbUrl = "";
    private static String dbUser = "";
    private static String dbPassword = "";
    private static String dbTable = "";
    //private static Sheet SHEET;
    private static List<Field> dbFields = new ArrayList<>();
    private static List<List<String>> tableData = new ArrayList<>();

    private static boolean containString(Sheet sheet, int row, int col) {
        if (sheet.getRow(row).getCell(col).getCellType() != STRING) {
            System.out.println("Cell at row: " + row + " and column: " + col + " must contain string!");
            System.exit(1);
        }
        return true;
    }

    public static String getDbUrl(Sheet sheet) {
        if (!dbUrl.isEmpty()) {
            return dbUrl;
        } else if (containString(sheet, 0,1 )) {
            dbUrl =  sheet.getRow(0).getCell(1).getStringCellValue();
        }
        return dbUrl;
    }

    public static String getDbUser(Sheet sheet) {
        if (!dbUser.isEmpty()) {
            return dbUser;
        } else if (containString(sheet, 1,1 )) {
            dbUser =  sheet.getRow(1).getCell(1).getStringCellValue();
        }
        return dbUser;
    }

    public static String getDbPassword(Sheet sheet) {
        if (!dbPassword.isEmpty()) {
            return dbPassword;
        } else if (containString(sheet, 2,1 )) {
            dbPassword =  sheet.getRow(2).getCell(1).getStringCellValue();
        }
        return dbPassword;
    }

    public static String getDbTableName(Sheet sheet) {
        if (!dbTable.isEmpty()) {
            return dbTable;
        } else if (containString(sheet, 3,0 )) {
            dbTable =  sheet.getRow(3).getCell(0).getStringCellValue();
        }
        return dbTable;
    }

    public static List<Field> getFields(Sheet sheet) {
        if (!dbFields.isEmpty()) {
            return dbFields;
        }

        List<Field> fields = new ArrayList<>();
        for (int curCol = 1; curCol <= getColCount(sheet); curCol++) {
            if (getCellStringData(sheet, curCol, 3).isEmpty()) {
                continue;
            }
            fields.add(new Field(
                    getCellStringData(sheet, curCol, 3),    //name
                    getCellStringData(sheet, curCol, 4),    //defValue
                    getCellStringData(sheet, curCol, 6),    //type
                    false,    //uniq
                    curCol    //colNumber
            ));
        }
        dbFields = fields;
        return dbFields;
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
                    cellData = Double.toString(cell.getNumericCellValue());
                    break;
                case BOOLEAN:
                    cellData = Boolean.toString(cell.getBooleanCellValue());
                    break;
                case FORMULA:
                    cellData = cell.getCellFormula();
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
        if (!tableData.isEmpty()) {
            return tableData;
        }

        List<List<String>> td = new ArrayList<>();
        for (int curRow = 9; curRow <= getRowCount(sheet); curRow++) {
            td.add(getRowData(sheet.getRow(curRow)));
        }
        tableData = td;
        return tableData;
    }

    public static List<String> getRowData(Row row) {
        List<String> rowData = new ArrayList<>();
        for (int cell = 1; cell <= dbFields.size(); cell++) {
            rowData.add(getCellStringData(row.getCell(cell)));
        }
        return rowData;
    }

    public static int getColCount(Sheet sheet) {
        return sheet.getRow(3).getLastCellNum();
    }

    public static int getRowCount(Sheet sheet) {
        return sheet.getLastRowNum();
    }
}
