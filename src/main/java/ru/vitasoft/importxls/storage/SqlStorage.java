package ru.vitasoft.importxls.storage;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.vitasoft.importxls.model.Field;
import ru.vitasoft.importxls.sql.SqlHelper;
import ru.vitasoft.importxls.util.DateTimeUtils;
import ru.vitasoft.importxls.util.ExcelUtils;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

public class SqlStorage {
    private final SqlHelper sqlHelper;
    private final Logger log = LogManager.getLogger();

    private String getDbType(String dbUrl) {
        return dbUrl.contains("postgresql") ? "org.postgresql.Driver" : "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    }

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        try {
            Class.forName(getDbType(dbUrl));
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    public void saveTableData(String tableName, List<Field> fields, List<List<String>> tableData) {
        sqlHelper.transactionalExecute(conn -> {
            StringBuilder sb = new StringBuilder();
            int curRow = 0;
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO " + tableName + " (" + ExcelUtils.getFieldsStr() + ") VALUES (" + ExcelUtils.getParamsStr() + ")")) {
                for (List<String> row : tableData) {
                    sb.delete(0, sb.length());
                    sb.append("Ошибка в строке: ").append(++curRow).append(" | ");
                    for (int i = 1; i <= row.size(); i++) {
                        switch (getFieldType(fields, i)) {
                            case "smallint":
                                ps.setShort(i, getShort(row, i));
                                break;
                            case "int":
                            case "serial":
                            case "integer":
                            case "bigserial":
                            case "bigint":
                                ps.setInt(i, getInteger(row, i));
                                break;
                            case "int8":
                                ps.setLong(i, getLong(row, i));
                                break;
                            case "float":
                            case "smallfloat":
                            case "double":
                                ps.setDouble(i, getDouble(row, i));
                                break;
                            case "date":
                                ps.setDate(i, getDate(row, i));
                                break;
                            case "datetime":
                                //ps.setTimestamp(i, getTimeStamp(row, i));
                                break;
                            case "boolean":
                                ps.setBoolean(i, getBoolean(row, i));
                                break;
                            case "char":
                            case "text":
                            case "nchar":
                            case "varchar":
                            case "lvarchar":
                            default:
                                ps.setString(i, row.get(i - 1));
                        }
                        sb.append(row.get(i - 1)).append(" | ");
                    }
                    if (ps.executeUpdate() == 0) {
                        log.log(Level.DEBUG, sb);
                    }
                }
            }
            return null;
        });
    }

    private static boolean isNumeric(String str){
        return str != null && str.matches("[0-9.]+");
    }

    private Date getDate(List<String> row, int i) {
        return DateTimeUtils.getDate(row.get(i - 1));
    }

    private Boolean getBoolean(List<String> row, int i) {
        if (isNumeric(row.get(i - 1).trim())) {
            return Integer.parseInt(row.get(i - 1).trim()) == 1;
        }
        return row.get(i - 1).contains("true");
    }

    private Short getShort(List<String> row, int i) {
        return isNumeric(row.get(i - 1).trim()) ? Short.parseShort(row.get(i - 1).trim()) : 0;
    }

    private Integer getInteger(List<String> row, int i) {
        return isNumeric(row.get(i - 1).trim()) ? Integer.parseInt(row.get(i - 1).trim()) : 0;
    }

    private Long getLong(List<String> row, int i) {
        return isNumeric(row.get(i - 1).trim()) ? Long.parseLong(row.get(i - 1).trim()) : 0;
    }

    private Double getDouble(List<String> row, int i) {
        return isNumeric(row.get(i - 1).trim()) ? Double.parseDouble(row.get(i - 1).trim()) : 0;
    }

    private String getFieldType(List<Field> fields, int i) {
        return fields.get(i - 1).getType().toLowerCase();
    }

}
