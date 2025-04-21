package site.debashisnaskar.rxflow.repository;

import com.google.gson.Gson;
import site.debashisnaskar.rxflow.dto.DoctorDto;
import site.debashisnaskar.rxflow.dto.UserDto;
import site.debashisnaskar.rxflow.model.Doctor;
import site.debashisnaskar.rxflow.model.Slot;
import site.debashisnaskar.rxflow.model.User;
import site.debashisnaskar.rxflow.utils.DB;
import site.debashisnaskar.rxflow.utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoctorRepository {

    private static final Connection connection;
    private static final Gson gson;
    private static final UserRepository userRepository;

    static {
        try {
            connection = DB.getConnection();
            gson = Utils.getGsonInstance();
            userRepository = new UserRepository();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Doctor> findAll() throws SQLException, ClassNotFoundException {
        List<Doctor> doctors = new ArrayList<>();
        Connection conn = DB.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM doctors d JOIN users u ON d.user_id = u.id");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            User user = userRepository.buildUser(rs, rs.getInt("user_id"));
            Doctor doctor = buildDoctor(rs, user);
            doctors.add(doctor);
        }

        return doctors;
    }

    public Doctor findById(int id) throws SQLException, ClassNotFoundException {
        Connection conn = DB.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM doctors d JOIN users u ON d.user_id = u.id where d.id = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        Doctor doctor = null;
        if (rs.next()) {
            User user = userRepository.buildUser(rs, rs.getInt("user_id"));
            doctor = buildDoctor(rs, user);
        }
        return doctor;
    }

    public void save(Doctor doctor) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO doctors(user_id,speciality,degree,experience,about,available,fees,slots_booked) VALUES(?,?,?,?,?,?,?,?)");
        stmt.setInt(1, doctor.getUser().getId());
        stmt.setString(2, doctor.getSpeciality());
        stmt.setString(3, doctor.getDegree());
        stmt.setString(4, doctor.getExperience());
        stmt.setString(5, doctor.getAbout());
        stmt.setBoolean(6, doctor.isAvailable());
        stmt.setDouble(7,doctor.getFees());
        stmt.setString(8,gson.toJson(doctor.getSlotsBooked()));

        stmt.executeUpdate();

        stmt = connection.prepareStatement("UPDATE users SET role = ? WHERE id = ?");
        stmt.setString(1,"ROLE_DOCTOR");
        stmt.setInt(2, doctor.getUser().getId());

        stmt.executeUpdate();
    }

    public boolean delete(int doctorId) throws SQLException, ClassNotFoundException {
        Doctor doctor = findById(doctorId);
        return userRepository.deleteById(doctor.getUser().getId());
    }

    private Doctor buildDoctor(ResultSet rs, User user) throws SQLException {

        return Doctor.builder()
                .id(rs.getInt("d.id"))
                .speciality(rs.getString("speciality"))
                .degree(rs.getString("degree"))
                .experience(rs.getString("experience"))
                .about(rs.getString("about"))
                .available(rs.getBoolean("available"))
                .fees(rs.getDouble("fees"))
                .slotsBooked(gson.fromJson(rs.getString("slots_booked"), Slot.class))
                .user(
                        user
                )
                .build();
    }

    public boolean changeAvailability(int doctorId, boolean availability) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("UPDATE doctors SET available = ? WHERE id = ?");
        stmt.setBoolean(1, availability);
        stmt.setInt(2, doctorId);

        int i = stmt.executeUpdate();

        return i > 0;
    }

    public List<DoctorDto> getDoctorList() throws SQLException, ClassNotFoundException {

        List<DoctorDto> doctors = new ArrayList<>();
        Connection conn = DB.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM doctors d JOIN users u ON d.user_id = u.id");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            UserDto user = userRepository.buildUserDto(rs);
            DoctorDto doctor = buildDoctorDto(rs, user);
            doctors.add(doctor);
        }

        return doctors;
    }

    private DoctorDto buildDoctorDto(ResultSet rs, UserDto user) throws SQLException {

        return DoctorDto.builder()
                .id(rs.getInt("d.id"))
                .speciality(rs.getString("speciality"))
                .degree(rs.getString("degree"))
                .experience(rs.getString("experience"))
                .about(rs.getString("about"))
                .available(rs.getBoolean("available"))
                .fees(rs.getDouble("fees"))
                .slotsBooked(gson.fromJson(rs.getString("slots_booked"), Slot.class))
                .user(
                        user
                )
                .build();
    }
}
