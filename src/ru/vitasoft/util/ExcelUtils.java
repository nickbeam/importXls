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
    private static String dbTableName = "";
    private static Sheet excelSheet;
    private static List<Field> dbTableFields = new ArrayList<>();
    private static String fieldsStr = "";
    private static String paramsStr = "";
    private static List<List<String>> dbTableData = new ArrayList<>();

    private static boolean containString(int row, int col) {
        if (excelSheet.getRow(row).getCell(col).getCellType() != STRING) {
            System.out.println("Cell at row: " + row + " and column: " + col + " must contain string!");
            System.exit(1);
        }
        return true;
    }

    public static String getDbUrl() {
        if (!dbUrl.isEmpty()) {
            return dbUrl;
        } else if (containString(0,1 )) {
            dbUrl =  excelSheet.getRow(0).getCell(1).getStringCellValue();
        }
        return dbUrl;
    }

    public static String getDbUser() {
        if (!dbUser.isEmpty()) {
            return dbUser;
        } else if (containString(1,1 )) {
            dbUser =  excelSheet.getRow(1).getCell(1).getStringCellValue();
        }
        return dbUser;
    }

    public static String getDbPassword() {
        if (!dbPassword.isEmpty()) {
            return dbPassword;
        } else if (containString(2,1 )) {
            dbPassword =  excelSheet.getRow(2).getCell(1).getStringCellValue();
        }
        return dbPassword;
    }

    public static String getDbTableName() {
        if (!dbTableName.isEmpty()) {
            return dbTableName;
        } else if (containString(3,0 )) {
            dbTableName =  excelSheet.getRow(3).getCell(0).getStringCellValue();
        }
        return dbTableName;
    }

    public static List<Field> getFields() {
        if (!dbTableFields.isEmpty()) {
            return dbTableFields;
        }
        List<Field> fields = new ArrayList<>();
        for (int curCol = 1; curCol <= getColCount(); curCol++) {
            if (getCellData(curCol, 3).isEmpty()) {
                continue;
            }
            fields.add(new Field(
                    getCellData(curCol, 3),    //name
                    getCellData(curCol, 4),    //defValue
                    getCellData(curCol, 6),    //type
                    false,    //uniq
                    curCol    //colNumber
            ));
        }
        dbTableFields = fields;
        return dbTableFields;
    }

    public static String getFieldsStr() {
        if (!fieldsStr.isEmpty()) {
            return fieldsStr;
        }
        StringBuilder fieldsString = new StringBuilder();
        StringBuilder fieldsParam = new StringBuilder();
        List<Field> fields = getFields();
        for (Field field : getFields()) {
            fieldsString.append(field.getName());
            fieldsParam.append("?");
            if (fields.size() != fields.indexOf(field) + 1) {
                fieldsString.append(", ");
                fieldsParam.append(", ");
            }
        }
        fieldsStr = fieldsString.toString();
        paramsStr = fieldsParam.toString();
        return fieldsStr;
    }

    public static void setSheet(Sheet sheet) {
        excelSheet = sheet;
    }

    public static String getParamsStr() {
        if (!paramsStr.isEmpty()) {
            return paramsStr;
        }
        getFieldsStr();
        return paramsStr;
    }

    public static String getCellData(int colNum, int rowNum) {
        Row row = excelSheet.getRow(rowNum);
        Cell cell = row.getCell(colNum);
        String cellData = "";
        if (cell == null) {
            return cellData;
        }
        try {
            cellData = cell.getStringCellValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cellData;
    }

    public static String getCellData(Cell cell) {
        String cellData = "";
        if (cell == null) {
            return cellData;
        }
        try {
            switch (cell.getCellType()) {
                case STRING:
                    cellData = cell.getStringCellValue();
                    break;
                case BLANK:
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

    public static List<List<String>> getTableData() {
        if (!dbTableData.isEmpty()) {
            return dbTableData;
        }

        List<List<String>> td = new ArrayList<>();
        for (int curRow = 9; curRow <= getRowCount(); curRow++) {
            td.add(getRowData(excelSheet.getRow(curRow)));
        }
        dbTableData = td;
        return dbTableData;
    }

    public static List<String> getRowData(Row row) {
        List<String> rowData = new ArrayList<>();
        for (int cell = 1; cell <= dbTableFields.size(); cell++) {
            rowData.add(getCellData(row.getCell(cell)));
        }
        return rowData;
    }

    public static int getColCount() {
        return excelSheet.getRow(3).getLastCellNum();
    }

    public static int getRowCount() {
        return excelSheet.getLastRowNum();
    }
}
