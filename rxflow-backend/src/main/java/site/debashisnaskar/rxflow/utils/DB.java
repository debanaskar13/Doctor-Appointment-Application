package site.debashisnaskar.rxflow.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

    private static Connection conn;

    public static Connection getConnection() throws SQLException, ClassNotFoundException {

        String database = "rxflow";
        String url = "jdbc:mysql://localhost:3307/"+database+"?useSSL=false";
        String user = "root";
        String password = "root";

        Class.forName("com.mysql.cj.jdbc.Driver");

        if(conn == null) {
            conn = DriverManager.getConnection(url,user, password);
        }
        return conn;
    }
}
