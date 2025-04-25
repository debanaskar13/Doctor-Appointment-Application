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

    public boolean bookAppointment(BookAppointmentRequest appointmentRequest) throws SQLException, ClassNotFoundException {
        String slotDate = appointmentRequest.getSlotDate();
        String slotTime = appointmentRequest.getSlotTime();
        int userId = appointmentRequest.getUserId();
        int doctorId = appointmentRequest.getDoctorId();

        Doctor doctor = doctorRepository.findById(doctorId);


        if(doctor == null) {
            throw new RuntimeException("Doctor not found");
        }

        if(!doctor.isAvailable()){
            throw new RuntimeException("Doctor is not available");
        }

        if(doctor.getUser().getId() == userId){
            throw new RuntimeException("Doctor and Patient are the same");
        }

//        If user try to book an appointment for the same day same time but different doctor
        List<Appointment> appointmentsByUser = userRepository.getAppointmentsByUser(userId);

        for(Appointment appointment : appointmentsByUser){
            String slotDateByUser = appointment.getSlotDate();
            String slotTimeByUser = appointment.getSlotTime();
            if(appointmentRequest.getSlotDate().equals(slotDateByUser) && appointmentRequest.getSlotTime().equals(slotTimeByUser) && !appointment.isCancelled() && !appointment.isCompleted()){
                throw new RuntimeException("You already have an appointment for the same slot");
            }
        }

        Map<String, ArrayList<String>> slotsBooked = doctor.getSlotsBooked() == null ? new HashMap<>() : doctor.getSlotsBooked();

        if(slotsBooked.get(slotDate) != null && !slotsBooked.get(slotDate).isEmpty()){
            if(slotsBooked.get(slotDate).contains(slotTime)){
                throw new RuntimeException("Slot not available");
            }else{
                slotsBooked.get(slotDate).add(slotTime);
            }
        }else {
            slotsBooked.put(slotDate,new ArrayList<>());
            slotsBooked.get(slotDate).add(slotTime);
        }

        User user = userRepository.findById(userId);

        Appointment appointment = Appointment.builder()
                .user(user)
                .doctor(doctor)
                .amount(doctor.getFees())
                .slotDate(slotDate)
                .slotTime(slotTime)
                .date(LocalDateTime.now().toString())
                .build();

        if(!userRepository.checkAppointment(appointment)) {
            throw new RuntimeException("Appointment already booked");
        }
        userRepository.bookAppointment(appointment);

        doctorRepository.updateSlotBooked(doctorId,slotsBooked);

        return true;
    }

    public List<Appointment> getAppointmentsByUserId(int userId) throws SQLException {
        return userRepository.getAppointmentsByUser(userId);
    }

    public boolean cancelAppointment(int userId, int appointmentId) throws SQLException {

        Appointment appointment = userRepository.findAppointmentById(appointmentId);

        if(appointment == null) {
            throw new RuntimeException("Appointment not found");
        }

        if(userId != appointment.getUser().getId()){
            throw new RuntimeException("User id mismatch : can't cancel appointment");
        }

        boolean isCancelled = userRepository.cancelAppointment(appointmentId);

        Map<String, ArrayList<String>> slotsBooked = appointment.getDoctor().getSlotsBooked();

        ArrayList<String> slotsBookedByDate = slotsBooked.get(appointment.getSlotDate());
        slotsBookedByDate.remove(appointment.getSlotTime());

        slotsBooked.put(appointment.getSlotDate(),slotsBookedByDate);

        boolean isUpdated = doctorRepository.updateSlotBooked(appointment.getDoctor().getId(), slotsBooked);


        return isCancelled && isUpdated;
    }
}
