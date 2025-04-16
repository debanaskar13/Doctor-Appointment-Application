package site.debashisnaskar.rxflow.utils;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

    private static Connection conn;
    private static final Dotenv dotenv = Dotenv.load();

    public static Connection getConnection() throws SQLException, ClassNotFoundException {

        String database = dotenv.get("DATABASE_NAME");
        String url = dotenv.get("DATABASE_URL")+database+"?useSSL=false";
        String user = dotenv.get("DATABASE_USERNAME");
        String password = dotenv.get("DATABASE_PASSWORD");

        Class.forName("com.mysql.cj.jdbc.Driver");

        if(conn == null) {
            conn = DriverManager.getConnection(url,user, password);
        }
        return conn;
    }
}
