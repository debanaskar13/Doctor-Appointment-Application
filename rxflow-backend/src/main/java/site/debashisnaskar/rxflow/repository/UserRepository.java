package site.debashisnaskar.rxflow.repository;

import com.google.gson.Gson;
import site.debashisnaskar.rxflow.model.Address;
import site.debashisnaskar.rxflow.model.User;
import site.debashisnaskar.rxflow.utils.DB;
import site.debashisnaskar.rxflow.utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private static final Connection connection;
    private static final Gson gson;

    static {
        try {
            connection = DB.getConnection();
            gson = Utils.getGsonInstance();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            User user = buildUser(rs, rs.getInt("id"));
            users.add(user);
        }
        return users;
    }

    public User findById(int id) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE id = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return buildUser(rs, rs.getInt("id"));
        }
        return null;
    }

    public void save(User user) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO users(username,password,name,image) VALUES ( ?, ?, ?, ?)");
        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getPassword());
        stmt.setString(3, user.getName());
        stmt.setString(4, user.getImage());

        stmt.executeUpdate();
    }

    public void update(User user) throws SQLException {

    }

    public boolean deleteById(int id) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM doctors WHERE user_id = ?");
        stmt.setInt(1, id);
        int i = stmt.executeUpdate();

        stmt = connection.prepareStatement("DELETE FROM users WHERE id = ?");
        stmt.setInt(1, id);
        int i1 = stmt.executeUpdate();
        return i > 0 && i1 > 0;
    }

    public User buildUser(ResultSet rs, int id) throws SQLException {
        return User.builder()
                .id(id)
                .username(rs.getString("username"))
                .email(rs.getString("email"))
                .phone(rs.getString("phone"))
                .name(rs.getString("name"))
                .role(rs.getString("role"))
                .image(rs.getString("image"))
                .gender(rs.getString("gender"))
                .dob(rs.getString("dob"))
                .address(gson.fromJson(rs.getString("address"), Address.class))
                .build();
    }

    public boolean updateImage(String imageUrl, int userId) throws SQLException {

        PreparedStatement stmt = connection.prepareStatement("UPDATE users SET image = ? WHERE id = ?");
        stmt.setString(1, imageUrl);
        stmt.setInt(2, userId);

        int i = stmt.executeUpdate();
        return i > 0;
    }
}
