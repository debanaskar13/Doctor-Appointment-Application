package site.debashisnaskar.rxflow.service;


import org.mindrot.jbcrypt.BCrypt;
import site.debashisnaskar.rxflow.dto.LoginRequest;
import site.debashisnaskar.rxflow.dto.LoginResponse;
import site.debashisnaskar.rxflow.dto.RegisterRequest;
import site.debashisnaskar.rxflow.model.User;
import site.debashisnaskar.rxflow.repository.UserRepository;
import site.debashisnaskar.rxflow.utils.DB;
import site.debashisnaskar.rxflow.utils.JwtUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {

    private static final UserService userService = new UserService();

    public int register(RegisterRequest registerRequest , String defaultImageUrl) throws IOException, ClassNotFoundException {

        try{
            Connection conn = DB.getConnection();
            String hashPassword = BCrypt.hashpw(registerRequest.getPassword(), BCrypt.gensalt());

            User user = User.builder()
                    .username(registerRequest.getUsername())
                    .password(hashPassword)
                    .name(registerRequest.getName())
                    .image(defaultImageUrl)
                    .role("ROLE_USER")
                    .address(registerRequest.getAddress())
                    .phone(registerRequest.getPhone() != null ? registerRequest.getPhone() : "0000000000")
                    .dob(registerRequest.getDob() != null ? registerRequest.getDob() : "Not Selected" )
                    .gender(registerRequest.getGender() != null ? registerRequest.getGender() : "Not Selected")
                    .build();

            return userService.addUser(user);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    public LoginResponse login(LoginRequest loginRequest) throws SQLException, ClassNotFoundException {
        Connection conn = DB.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? ");
        stmt.setString(1, loginRequest.getUsername());
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String pass = rs.getString("password");
            String role = rs.getString("role");

            User user = User.builder().username(loginRequest.getUsername()).role(role).build();

            if(BCrypt.checkpw(loginRequest.getPassword(), pass)) {
//                    Call JWT here
                String token = JwtUtil.generateToken(user);
                return LoginResponse.builder()
                        .success(true)
                        .token(token)
                        .role(role)
                        .build();
            }
        }
        return null;
    }
}
