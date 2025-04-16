package site.debashisnaskar.rxflow.service;

import site.debashisnaskar.rxflow.model.User;
import site.debashisnaskar.rxflow.repository.UserRepository;

import java.sql.SQLException;
import java.util.List;

public class UserService {

    private static final UserRepository userRepository = new UserRepository();


    public List<User> findAllUsers() {
        try {
            return userRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findUserById(int userId) {
        try {
            return userRepository.findById(userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
