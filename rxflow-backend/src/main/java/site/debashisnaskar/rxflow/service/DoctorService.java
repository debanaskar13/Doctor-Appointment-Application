package site.debashisnaskar.rxflow.service;

import site.debashisnaskar.rxflow.model.Address;
import site.debashisnaskar.rxflow.model.Doctor;
import site.debashisnaskar.rxflow.model.User;
import site.debashisnaskar.rxflow.utils.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoctorService {

    public void AddDoctor(Doctor doctor) {

    }

    public boolean DeleteDoctor(int doctorId) throws SQLException, ClassNotFoundException {

        String query = "DELETE FROM doctors WHERE id = ?";
        Connection conn = DB.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1,doctorId);
        int i = stmt.executeUpdate();
        return i>0;
    }

    public void UpdateDoctor(Doctor doctor) {

    }

    public List<Doctor> getAllDoctors() throws SQLException, ClassNotFoundException {
        List<Doctor> doctors = new ArrayList<Doctor>();
        String query = "SELECT * FROM doctors d JOIN users u ON d.user_id = u.id;";
        Connection conn = DB.getConnection();
        ResultSet rs = conn.prepareStatement(query).executeQuery();
        while (rs.next()) {
            Doctor doctor = Doctor.builder()
                    .id(rs.getInt("d.id"))
                    .speciality(rs.getString("speciality"))
                    .degree(rs.getString("degree"))
                    .experience(rs.getString("experience"))
                    .about(rs.getString("about"))
                    .available(rs.getBoolean("available"))
                    .fees(rs.getDouble("fees"))
                    .user(
                            User.builder()
                                    .id(rs.getInt("u.id"))
                                    .username(rs.getString("username"))
                                    .email(rs.getString("email"))
                                    .phone(rs.getString("phone"))
                                    .name(rs.getString("name"))
                                    .role(rs.getString("role"))
                                    .image(rs.getString("image"))
                                    .gender(rs.getString("gender"))
                                    .dob(rs.getString("dob"))
                                    .address(rs.getObject("address",Address.class))
                                    .build()
                    )
                    .build();

            doctors.add(doctor);
        }
        return doctors;
    }

    public Doctor getDoctor(String id) {
        return null;
    }


}
