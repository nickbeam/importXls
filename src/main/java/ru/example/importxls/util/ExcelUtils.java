package ru.example.importxls.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.NumberToTextConverter;
import ru.example.importxls.model.Field;

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
    private static final Map<String, Set<String>> UNIQ_DATA_MAP = new HashMap<>();
    private static final Logger log = LogManager.getLogger();

    private static boolean containString(int row, int col) {
        if (excelSheet.getRow(row).getCell(col).getCellType() != STRING) {
            System.out.println("Cell at row: " + row + " and column: " + col + " must contain string!");
            System.exit(1);
        }
        return true;
    }

    public static String getDbUrl(Sheet sheet) {
        setSheet(sheet);
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
            Field field = new Field(
                    getCellData(curCol, 3),    //name
                    getCellData(curCol, 4),    //defValue
                    getCellBoolean(curCol, 5),    //required
                    getCellData(curCol, 6).toLowerCase(),    //type
                    getCellBoolean(curCol, 8),    //uniq
                    curCol    //colNumber
            );
            fields.add(field);
            if (field.isUniq()) {
                UNIQ_DATA_MAP.put(field.getName(), new HashSet<>());
            }
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
            log.error(e.getMessage());
        }
        return cellData;
    }

    public static Boolean getCellBoolean(int colNum, int rowNum) {
        Row row = excelSheet.getRow(rowNum);
        Cell cell = row.getCell(colNum);
        String cellData = "";
        if (cell == null) {
            return false;
        }
        try {
            cellData = cell.getStringCellValue();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return cellData.toLowerCase().contains("да");
    }

    public static String getCellData(Cell cell, Field field) {
        String cellData = "";
        if (cell == null && !field.getRequired()) {
            return field.getDefValue();
        } else if (cell == null) {
            return null;
        }
        try {
            switch (Objects.requireNonNull(cell).getCellType()) {
                case STRING:
                    cellData = cell.getStringCellValue();
                    break;
                case BLANK:
                    return field.getRequired() ? null : field.getDefValue();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        cellData = cell.getDateCellValue().toString();
                        break;
                    }
                    cellData = NumberToTextConverter.toText(cell.getNumericCellValue());
                    break;
                case BOOLEAN:
                    cellData = Boolean.toString(cell.getBooleanCellValue());
                    break;
                case FORMULA:
                    cellData = cell.getCellFormula();
                    break;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        if (field.isUniq() && isUniqCellData(field, cellData)) {
            return null;
        }
        return cellData.isEmpty() ? field.getDefValue() : cellData;
    }

    private static boolean isUniqCellData(Field field, String cellData) {
        if (field.isUniq() && UNIQ_DATA_MAP.get(field.getName()).contains(cellData)) {
            String message = "Не уникальное значение: " + cellData + " в поле: " + field.getName();
            log.log(Level.DEBUG, message);
            System.out.println(message);
            return true;
        } else if (field.isUniq()) {
            Set<String> uniqValues = UNIQ_DATA_MAP.get(field.getName());
            uniqValues.add(cellData);
            UNIQ_DATA_MAP.put(field.getName(), uniqValues);
        }
        return false;
    }

    public static List<List<String>> getTableData() {
        if (dbTableFields.isEmpty()) {
            getFields();
        }
        if (!dbTableData.isEmpty()) {
            return dbTableData;
        }

        List<List<String>> td = new ArrayList<>();
        List<String> currentRowData;
        for (int curRow = 9; curRow <= getRowCount(); curRow++) {
            currentRowData = getRowData(excelSheet.getRow(curRow));
            if (currentRowData != null) {
                td.add(currentRowData);
            }
        }
        dbTableData = td;
        return dbTableData;
    }

    public static List<String> getRowData(Row row) {
        List<String> rowData = new ArrayList<>();
        Field currentField;
        String currentCellData;
        for (int cell = 1; cell <= dbTableFields.size(); cell++) {
            currentField = dbTableFields.get(cell - 1);
            currentCellData = getCellData(row.getCell(cell), currentField);
            if (currentCellData != null) {
                rowData.add(currentCellData);
            } else {
                return null;
            }
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
