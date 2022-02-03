package ru.vitasoft.storage;

import ru.vitasoft.sql.SqlHelper;
import ru.vitasoft.util.ExcelUtils;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

public class SqlStorage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        try {
            //Class.forName("org.postgresql.Driver");
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    public void saveTableData(String tableName, List<List<String>> tableData) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO " + tableName + " (" + ExcelUtils.getFieldsStr() + ") VALUES (" + ExcelUtils.getParamsStr() + ")")) {
                for (List<String> row : tableData) {
                    for (int i = 1; i <= row.size(); i++) {
                        ps.setString(i, row.get(i - 1));
                    }
                    ps.execute();
                }
            }
            return null;
        });
    }

}
