package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DB {

    private static Connection conn;

    private static Properties loadProperties() {
        try (FileInputStream fileInputStream = new FileInputStream("src/db.properties")) {

            Properties prop = new Properties();
            prop.load(fileInputStream);
            return prop;

        } catch (IOException e) {
            throw new DBException(e.getMessage());
        }
    }

    public static Connection getConnection() {
        if (conn == null) {
            Properties prop = loadProperties();
            String url = prop.getProperty("dburl");
            try {
                conn = DriverManager.getConnection(url, prop);
            } catch (SQLException e) {
                throw new DBException(e.getMessage());
            }
        }
        return conn;
    }

    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new DBException(e.getMessage());
            }
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new DBException(e.getMessage());
            }
        }
    }

    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                throw new DBException(e.getMessage());
            }
        }
    }
}
