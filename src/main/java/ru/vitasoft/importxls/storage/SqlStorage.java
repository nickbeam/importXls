package ru.vitasoft.importxls.storage;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.vitasoft.importxls.sql.SqlHelper;
import ru.vitasoft.importxls.util.ExcelUtils;

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

    public void saveTableData(String tableName, List<List<String>> tableData) {
        sqlHelper.transactionalExecute(conn -> {
            StringBuilder sb = new StringBuilder();
            int curRow = 0;
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO " + tableName + " (" + ExcelUtils.getFieldsStr() + ") VALUES (" + ExcelUtils.getParamsStr() + ")")) {
                for (List<String> row : tableData) {
                    sb.delete(0, sb.length());
                    sb.append("Ошибка в строке: ").append(++curRow).append(" | ");
                    for (int i = 1; i <= row.size(); i++) {
                        ps.setString(i, row.get(i - 1));
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

}
