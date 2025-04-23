package site.debashisnaskar.rxflow.utils;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DB {

    private static Connection conn;
    private static final Dotenv dotenv = Dotenv.load();
    private static final Logger logger = Logger.getLogger(DB.class.getName());

    public static Connection getConnection() throws SQLException, ClassNotFoundException {

        String database = dotenv.get("DATABASE_NAME");
        String url = dotenv.get("DATABASE_URL") + database;
        String user = dotenv.get("DATABASE_USERNAME");
        String password = dotenv.get("DATABASE_PASSWORD");

        Class.forName(dotenv.get("DATABASE_DRIVER_CLASS_NAME"));

        if(conn == null) {
            try{
                conn = DriverManager.getConnection(url,user, password);
            } catch (Exception e){
                logger.severe(e.getMessage());
            }
        }
        return conn;
    }
}
