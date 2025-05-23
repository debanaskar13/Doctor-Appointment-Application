package site.debashisnaskar.rxflow.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import site.debashisnaskar.rxflow.dto.Page;
import site.debashisnaskar.rxflow.dto.UserDto;
import site.debashisnaskar.rxflow.model.Address;
import site.debashisnaskar.rxflow.model.Appointment;
import site.debashisnaskar.rxflow.model.Doctor;
import site.debashisnaskar.rxflow.model.User;
import site.debashisnaskar.rxflow.utils.DB;
import site.debashisnaskar.rxflow.utils.Utils;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public Page<List<User>> findAll(Page<List<User>> page) throws SQLException {
        List<String> allowedColumns = List.of("id", "name", "username", "email");
        List<String> allowedDirections = List.of("ASC", "DESC");

        String sortBy = page.getSortBy();
        String direction = page.getSorDirection();

        if(!allowedColumns.contains(sortBy)) {
            sortBy = "id";
        }

        if(!allowedDirections.contains(direction)) {
            direction = "ASC";
        }

        List<User> users = new ArrayList<>();
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT * " +
                "FROM users " +
                "WHERE role = 'ROLE_USER' " +
                "ORDER BY " + sortBy + " " + direction + " " +
                "LIMIT ? OFFSET ?"
        );

        stmt.setLong(1,page.getPageSize());
        stmt.setLong(2,(page.getPageNumber() - 1) * page.getPageSize() + 1);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            User user = buildUser(rs, rs.getInt("id"));
            users.add(user);
        }
        page.setData(users);

        stmt = connection.prepareStatement("SELECT COUNT(*) FROM users WHERE role = 'ROLE_USER'");
        rs = stmt.executeQuery();

        if(rs.next()) {
            long count = rs.getLong("count");
            long totalPages = count % page.getPageSize() == 0 ? count / page.getPageSize() : count / page.getPageSize() + 1;
            page.setTotalPages(totalPages);
        }

        return page;
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

    public int save(User user) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO users(username,password,name,image,address,phone,dob,gender,email) VALUES ( ?, ?, ?, ?, ?::jsonb, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getPassword());
        stmt.setString(3, user.getName());
        stmt.setString(4, user.getImage());
        stmt.setString(5, gson.toJson(user.getAddress()));
        stmt.setString(6, user.getPhone());
        stmt.setString(7, user.getDob());
        stmt.setString(8, user.getGender());
        stmt.setString(9, user.getEmail());

        int rowsEffected = stmt.executeUpdate();

        if(rowsEffected > 0) {
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if(generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
        }

        return -1;
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

    public UserDto buildUserDto(ResultSet rs) throws SQLException {
        return UserDto.builder()
                .name(rs.getString("name"))
                .image(rs.getString("image"))
                .address(gson.fromJson(rs.getString("address"), Address.class))
                .build();
    }

    public User getUserProfile(String username) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return buildUser(rs, rs.getInt("id"));
        }
        return null;
    }

    public boolean updateUserProfile(User user) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("UPDATE users SET phone = ?, address = ?::jsonb, gender = ?, dob = ?, image = ?, name = ?, email = ? WHERE id = ?");
        stmt.setString(1, user.getPhone());
        stmt.setString(2, gson.toJson(user.getAddress()));
        stmt.setString(3, user.getGender());
        stmt.setString(4, user.getDob());
        stmt.setString(5, user.getImage());
        stmt.setString(6, user.getName());
        stmt.setString(7, user.getEmail());
        stmt.setInt(8, user.getId());

        int rowsEffected = stmt.executeUpdate();
        return rowsEffected > 0;
    }
}
