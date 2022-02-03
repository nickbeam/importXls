package ru.vitasoft.storage;

import ru.vitasoft.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

public class SqlStorage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    public void saveTableData(List<List<String>> tableData) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                ps.setString(1, tableData.get(0).get(0));
                ps.setString(2, tableData.get(1).get(1));
                ps.execute();
            }
            return null;
        });
    }

}
