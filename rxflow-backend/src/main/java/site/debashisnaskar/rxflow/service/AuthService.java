package site.debashisnaskar.rxflow.service;


import org.mindrot.jbcrypt.BCrypt;
import site.debashisnaskar.rxflow.dto.LoginRequest;
import site.debashisnaskar.rxflow.dto.RegisterRequest;
import site.debashisnaskar.rxflow.utils.DB;
import site.debashisnaskar.rxflow.utils.JwtUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {

    public AuthService(){}

    public void register(RegisterRequest registerRequest , String defaultImageUrl) throws IOException, SQLException, ClassNotFoundException {

        Connection conn = DB.getConnection();
        String hashPassword = BCrypt.hashpw(registerRequest.getPassword(), BCrypt.gensalt());


        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username,password,name,image) VALUES (?,?,?,?)");
        stmt.setString(1, registerRequest.getUsername());
        stmt.setString(2, hashPassword);
        stmt.setString(3, registerRequest.getName());
        stmt.setString(4,defaultImageUrl);

//        Insert User details
        stmt.executeUpdate();
    }

    public String login(LoginRequest loginRequest) throws SQLException, ClassNotFoundException {
        Connection conn = DB.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? ");
        stmt.setString(1, loginRequest.getUsername());
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String pass = rs.getString("password");
            if(BCrypt.checkpw(loginRequest.getPassword(), pass)) {
//                    Call JWT here
                return JwtUtil.generateToken(loginRequest.getUsername());
            }
        }
        return null;
    }
}
