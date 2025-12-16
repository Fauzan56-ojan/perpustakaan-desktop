package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=Perpustakaan;trustServerCertificate=true;";
    private static final String USER = "Fauzan"; // ganti dengan username SQL kamu
    private static final String PASSWORD = "Fauzan123"; // ganti dengan password kamu

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
