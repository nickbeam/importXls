package ru.vitasoft.importxls.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ISqlExecutor<T> {
    T execute(PreparedStatement st) throws SQLException;
}
