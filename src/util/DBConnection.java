package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.InputStream;

public class DBConnection {
    private static Connection connection;
    private static Properties props;

    private static void loadProperties() {
        try {
            if (props == null) {
                props = new Properties();
                InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("database.properties");
                props.load(input);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load database properties", e);
        }
    }

    public static Connection getConnection() {
        try {
            loadProperties();
            
            // Check if connection is closed or null
            if (connection == null || connection.isClosed()) {
                String url = props.getProperty("db.url");
                String user = props.getProperty("db.user");
                String password = props.getProperty("db.password");
                String driver = props.getProperty("db.driver");
                
                Class.forName(driver);
                connection = DriverManager.getConnection(url, user, password);
            }
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to establish database connection", e);
        }
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
                connection = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

