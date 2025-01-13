package ru.example.importxls.sql;

import java.sql.Connection;
import java.sql.SQLException;

public interface ISqlTransaction<T> {
    T execute(Connection conn) throws SQLException;
}
