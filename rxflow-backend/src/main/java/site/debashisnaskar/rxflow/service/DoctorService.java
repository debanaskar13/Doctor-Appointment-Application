package site.debashisnaskar.rxflow.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Part;
import org.mindrot.jbcrypt.BCrypt;
import site.debashisnaskar.rxflow.exception.DoctorNotFoundException;
import site.debashisnaskar.rxflow.exception.UserNotFoundException;
import site.debashisnaskar.rxflow.model.Doctor;
import site.debashisnaskar.rxflow.model.User;
import site.debashisnaskar.rxflow.repository.DoctorRepository;
import site.debashisnaskar.rxflow.repository.UserRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DoctorService {

    private static final DoctorRepository doctorRepository = new DoctorRepository();
    private static final UserRepository userRepository = new UserRepository();
    private static final UserService userService = new UserService();

    public void addDoctor(Doctor doctor,Part filePart, String defaultImageUrl) throws SQLException, ClassNotFoundException, UserNotFoundException, IOException, ServletException {

        int userId = doctor.getUser().getId();
        String imageUrl = null;

        User user = userRepository.findById(userId);
        if(user == null) {

//            Create a new User
            userId = userService.addUser(User.builder()
                    .username(doctor.getUser().getUsername())
                    .password(BCrypt.hashpw(doctor.getUser().getPassword(), BCrypt.gensalt()))
                    .role("ROLE_DOCTOR")
                    .name(doctor.getUser().getName())
                    .image(defaultImageUrl)
                            .address(doctor.getUser().getAddress())
                            .phone(doctor.getUser().getPhone() != null ? doctor.getUser().getPhone() : "0000000000")
                            .dob(doctor.getUser().getDob() != null ? doctor.getUser().getDob() : "Not Selected" )
                            .gender(doctor.getUser().getGender() != null ? doctor.getUser().getGender() : "Not Selected")
                    .build());

            if(userId != -1) {
                user = User.builder().id(userId).build();
            }
        }

        if(filePart != null) {
            userService.uploadImage(filePart,user.getId());
        }

        doctor.setUser(user);
        doctorRepository.save(doctor);
    }

    public boolean deleteDoctor(int doctorId) throws SQLException, ClassNotFoundException {
        return doctorRepository.delete(doctorId);
    }

    public boolean changeAvailability(int doctorId) throws SQLException, ClassNotFoundException {
        Doctor doctor = doctorRepository.findById(doctorId);
        return doctorRepository.changeAvailability(doctorId, !doctor.isAvailable());
    }

    public void updateDoctor(Doctor doctor) throws SQLException, ClassNotFoundException, DoctorNotFoundException {

    }

    public List<Doctor> getAllDoctors(){
        try {
            return doctorRepository.findAll();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Doctor getDoctor(int id) throws SQLException, ClassNotFoundException, DoctorNotFoundException {
        Doctor doctor = doctorRepository.findById(id);
        if(doctor == null) {
            throw new DoctorNotFoundException("doctor not found");
        }
        return doctor;
    }

    public boolean uploadImage(Part part, int doctorId) throws SQLException, DoctorNotFoundException, ClassNotFoundException, ServletException, IOException {
        Doctor doctor = getDoctor(doctorId);
        return userService.uploadImage(part,doctor.getUser().getId());
    }
}
