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
import java.sql.Types;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

    public int saveTableData(String tableName, List<Field> fields, List<List<String>> tableData) {
        StringBuilder sb = new StringBuilder();
        int curRow = 0;
        AtomicInteger insRowCount = new AtomicInteger();
        for (List<String> row : tableData) {
            sb.delete(0, sb.length());
            sb.append("Ошибка в строке: ").append(++curRow).append(" | ");
            sqlHelper.transactionalExecute(conn -> {
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO " + tableName + " (" + ExcelUtils.getFieldsStr() + ") VALUES (" + ExcelUtils.getParamsStr() + ")")) {
                    for (int i = 1; i <= row.size(); i++) {
                        String cellValue = row.get(i - 1).trim();
                        switch (getFieldType(fields, i)) {
                            case "smallint":
                                ps.setShort(i, getShort(cellValue));
                                break;
                            case "int":
                            case "serial":
                            case "integer":
                            case "bigserial":
                            case "bigint":
                                ps.setInt(i, getInteger(cellValue));
                                break;
                            case "int8":
                                ps.setLong(i, getLong(cellValue));
                                break;
                            case "float":
                            case "smallfloat":
                            case "double":
                                ps.setDouble(i, getDouble(cellValue));
                                break;
                            case "date":
                                Date date = getDate(cellValue);
                                if (date == null) {
                                    ps.setNull(i, Types.DATE);
                                    break;
                                }
                                ps.setDate(i, date);
                                break;
                            case "datetime":
                                //ps.setTimestamp(i, getTimeStamp(cellValue));
                                break;
                            case "boolean":
                                ps.setBoolean(i, getBoolean(cellValue));
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
                    if (ps.executeUpdate() > 0) {
                        insRowCount.getAndIncrement();
                    }
                } catch (Exception e) {
                    log.log(Level.DEBUG, sb + " по причине: " + e.getMessage());
                    System.out.println(sb + " по причине: " + e.getMessage());
                }
                return null;
            });
        }
        return insRowCount.intValue();
    }

    private static boolean isNumeric(String str) {
        return str != null && str.matches("[0-9.]+");
    }

    private Date getDate(String str) {
        return str.isEmpty() ? null : DateTimeUtils.getDate(str);
    }

    private Boolean getBoolean(String str) {
        return isNumeric(str) ? Integer.parseInt(str) == 1 :str.contains("true");
    }

    private Short getShort(String str) {
        return isNumeric(str) ? Short.parseShort(str) : 0;
    }

    private Integer getInteger(String str) {
        return isNumeric(str) ? Integer.parseInt(str) : 0;
    }

    private Long getLong(String str) {
        return isNumeric(str) ? Long.parseLong(str) : 0;
    }

    private Double getDouble(String str) {
        return isNumeric(str) ? Double.parseDouble(str) : 0;
    }

    private String getFieldType(List<Field> fields, int i) {
        return fields.get(i - 1).getType().toLowerCase();
    }

}
