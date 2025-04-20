package site.debashisnaskar.rxflow.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import site.debashisnaskar.rxflow.exception.UserNotFoundException;
import site.debashisnaskar.rxflow.model.User;
import site.debashisnaskar.rxflow.repository.UserRepository;
import site.debashisnaskar.rxflow.utils.CloudinaryUtil;
import site.debashisnaskar.rxflow.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class UserService {

    private static final UserRepository userRepository = new UserRepository();
    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    public int addUser(User user) throws SQLException {
        return userRepository.save(user);
    }

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

    public boolean uploadImage(Part part,int userId) throws ServletException, IOException, SQLException {
        String imageUrl = uploadToCloudinary(part);
        return userRepository.updateImage(imageUrl, userId);
    }

    public String uploadToCloudinary(Part part) throws IOException {
        File file = Utils.convertPartToFile(part);
        Cloudinary cloudinary = CloudinaryUtil.getInstance();

        Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());

        String imageUrl = uploadResult.get("secure_url").toString();
        file.delete();

        return imageUrl;
    }
}
