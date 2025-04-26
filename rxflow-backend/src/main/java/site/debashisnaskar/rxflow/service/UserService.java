package site.debashisnaskar.rxflow.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import site.debashisnaskar.rxflow.dto.BookAppointmentRequest;
import site.debashisnaskar.rxflow.dto.UpdateProfileRequest;
import site.debashisnaskar.rxflow.exception.UserNotFoundException;
import site.debashisnaskar.rxflow.model.Appointment;
import site.debashisnaskar.rxflow.model.Doctor;
import site.debashisnaskar.rxflow.model.User;
import site.debashisnaskar.rxflow.repository.DoctorRepository;
import site.debashisnaskar.rxflow.repository.UserRepository;
import site.debashisnaskar.rxflow.utils.CloudinaryUtil;
import site.debashisnaskar.rxflow.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class UserService {

    private static final UserRepository userRepository = new UserRepository();
    private static final DoctorRepository doctorRepository = new DoctorRepository();
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

    public boolean uploadImage(Part part,int userId) throws IOException, SQLException {
        String imageUrl = CloudinaryUtil.uploadToCloudinary(part);
        return userRepository.updateImage(imageUrl, userId);
    }



    public User getUserProfile(User user) throws SQLException {
        return userRepository.getUserProfile(user.getUsername());
    }

    public boolean updateUserProfile(User user, Part imageFilePart) throws SQLException, IOException {

        String imageUrl = null;
        if(imageFilePart != null) {
             imageUrl = CloudinaryUtil.uploadToCloudinary(imageFilePart);
            String[] imageUrlSplit = user.getImage().split("/");
            String deleteImagePublicId = imageUrlSplit[imageUrlSplit.length - 1].split("\\.")[0];
            CloudinaryUtil.deleteFromCloudinary(deleteImagePublicId);
        }
        user.setImage(imageUrl == null ? user.getImage() : imageUrl);
        
        return userRepository.updateUserProfile(user);
    }


    public boolean updateUser(User user) throws SQLException {
        return userRepository.updateUserProfile(user);
    }
}
