package site.debashisnaskar.rxflow.service;


import org.mindrot.jbcrypt.BCrypt;
import site.debashisnaskar.rxflow.dto.LoginRequest;
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

    private static final UserRepository userRepository = new UserRepository();

    public void register(RegisterRequest registerRequest , String defaultImageUrl) throws IOException, ClassNotFoundException {

        try{
            Connection conn = DB.getConnection();
            String hashPassword = BCrypt.hashpw(registerRequest.getPassword(), BCrypt.gensalt());

            User user = User.builder()
                    .username(registerRequest.getUsername())
                    .password(hashPassword)
                    .name(registerRequest.getUsername())
                    .image(defaultImageUrl)
                    .build();

            userRepository.save(user);
        }catch (SQLException e){
            e.printStackTrace();
        }
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
