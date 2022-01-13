package ru.vitasoft.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.*;

import static org.apache.poi.ss.usermodel.CellType.*;

public class ExcelUtil {
    public static String getDbTableName(Sheet sheet) {
        if (sheet.getRow(2).getCell(0).getCellType() != STRING) {
            System.out.println("DB table name is empty, check xls file");
            System.exit(1);
        }
        return sheet.getRow(2).getCell(0).getStringCellValue();
    }

    public static Map<Integer, List<String>> getTableData(Sheet sheet) {

//        Map<Integer, List<String>> tableData = new HashMap<>();
//        int i = 0;
//        for (Row row : sheet) {
//            tableData.put(i, new ArrayList<String>());
//            for (Cell cell : row) {
//                switch (cell.getCellType()) {
//                    case STRING:
//                        tableData.put(i, )
//                        break;
//                    case NUMERIC: ... break;
//                    case BOOLEAN: ... break;
//                    case FORMULA: ... break;
//                    default: tableData.get(new Integer(i)).add(" ");
//                }
//            }
//            i++;
//        }

        for (Row row : sheet) {
            Iterator<Cell> cellIterator = row.cellIterator();   //iterating over each column
            for (Cell cell : row) {
                switch (cell.getCellType()) {
                    case STRING:    //field that represents string cell type
                        System.out.print(cell.getStringCellValue() + "\t\t\t");
                        break;
                    case NUMERIC:    //field that represents number cell type
                        System.out.print(cell.getNumericCellValue() + "\t\t\t");
                        break;
                    default:
                        System.out.println("Not String or Numeric");
                }
            }
            System.out.println("");
        }

        return null;
    }
}
