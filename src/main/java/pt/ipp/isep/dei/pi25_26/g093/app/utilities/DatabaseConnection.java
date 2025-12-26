package pt.ipp.isep.dei.pi25_26.g093.app.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:oracle:thin:@vsgate-s1.dei.isep.ipp.pt:10938:XE";

    private static final String USER = "system";

    private static final String PASSWORD = "egdcps123nadmtt";

    static {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Oracle JDBC Driver não encontrado!", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}