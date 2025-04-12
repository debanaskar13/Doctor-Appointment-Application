package site.debashisnaskar.rxflow.service;


import org.mindrot.jbcrypt.BCrypt;
import site.debashisnaskar.rxflow.utils.DB;
import site.debashisnaskar.rxflow.dto.LoginRequest;
import site.debashisnaskar.rxflow.dto.RegisterRequest;
import site.debashisnaskar.rxflow.utils.JwtUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthService {

    public AuthService(){}

    public void register(RegisterRequest registerRequest) throws IOException, SQLException, ClassNotFoundException {

        Connection conn = DB.getConnection();
        String hashPassword = BCrypt.hashpw(registerRequest.getPassword(), BCrypt.gensalt());

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username,password,email,phone,name) VALUES (?,?,?,?,?)");
        stmt.setString(1, registerRequest.getUsername());
        stmt.setString(2, hashPassword);
        stmt.setString(3, registerRequest.getEmail());
        stmt.setString(4, registerRequest.getPhone());
        stmt.setString(5, registerRequest.getName());

//        Insert User details
        stmt.executeUpdate();

        stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? ");
        stmt.setString(1, registerRequest.getUsername());
        ResultSet rs = stmt.executeQuery();
        int userId = -1;
        if(rs.next()) {
            userId = rs.getInt("id");
        }

        stmt = conn.prepareStatement("INSERT INTO roles (user_id,role) VALUES (?,?)");
        if(userId > 0){
            stmt.setInt(1,userId);
            stmt.setString(2,"ROLE_USER");
        }

//        Insert Role

        stmt.executeUpdate();

    }

    public String login(LoginRequest loginRequest) throws SQLException, ClassNotFoundException {
        Connection conn = DB.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? ");
        stmt.setString(1, loginRequest.getUsername());
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String pass = rs.getString("password");
            int id = rs.getInt("id");
            if(BCrypt.checkpw(loginRequest.getPassword(), pass)) {
                List<String> roles = getUserRoles(conn, id);

//                    Call JWT here
                return JwtUtil.generateToken(loginRequest.getUsername());
            }
        }
        return null;
    }

    private List<String> getUserRoles(Connection conn, int userId) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT role FROM roles WHERE user_id = ? ");
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            ArrayList<String> roles = new ArrayList<>();
            while(rs.next()) {
                roles.add(rs.getString("role"));
            }

            return roles;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
