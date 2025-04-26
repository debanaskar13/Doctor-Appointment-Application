package site.debashisnaskar.rxflow.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import java.util.UUID;

public class AppointmentRepository {

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


    public boolean bookAppointment(Appointment appointment) throws SQLException {

        PreparedStatement stmt = connection.prepareStatement("INSERT INTO appointments(user_id,doctor_id,slot_date,slot_time,amount) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        stmt.setInt(1, appointment.getUser().getId());
        stmt.setInt(2, appointment.getDoctor().getId());
        stmt.setString(3, appointment.getSlotDate());
        stmt.setString(4, appointment.getSlotTime());
        stmt.setDouble(5,appointment.getAmount());

        int rowsEffected = stmt.executeUpdate();

        return rowsEffected > 0;
    }

    public boolean checkAppointment(Appointment appointment) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM appointments WHERE doctor_id = ? AND slot_date = ? AND slot_time = ? ");
        stmt.setInt(1, appointment.getDoctor().getId());
        stmt.setString(2, appointment.getSlotDate());
        stmt.setString(3, appointment.getSlotTime());

        ResultSet resultSet = stmt.executeQuery();

        if(resultSet.next()) {
            return false;
        }
        return true;
    }

    public List<Appointment> getAppointmentsByUser(int userId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT *  FROM appointments a\n" +
                "JOIN doctors d on d.id = a.doctor_id\n" +
                "JOIN users u on d.user_id = u.id\n" +
                "WHERE a.user_id = ?\n" +
                "ORDER BY a.date DESC\n");

        stmt.setInt(1, userId);

        ResultSet rs = stmt.executeQuery();

        List<Appointment> appointments = new ArrayList<>();
        while(rs.next()) {

            Appointment appointment = Appointment.builder()
                    .id(UUID.fromString(rs.getString("id")))
                    .user(User.builder()
                            .id(rs.getInt("user_id"))
                            .name(rs.getString("name"))
                            .address(gson.fromJson(rs.getString("address"), Address.class))
                            .image(rs.getString("image"))
                            .build())
                    .doctor(Doctor.builder()
                            .id(rs.getInt("doctor_id"))
                            .speciality(rs.getString("speciality"))
                            .degree(rs.getString("degree"))
                            .fees(rs.getDouble("fees"))
                            .available(rs.getBoolean("available"))
                            .experience(rs.getString("experience"))
                            .about(rs.getString("about"))
                            .build())
                    .slotDate(rs.getString("slot_date"))
                    .slotTime(rs.getString("slot_time"))
                    .amount(rs.getDouble("amount"))
                    .date(rs.getString("date"))
                    .cancelled(rs.getBoolean("cancelled"))
                    .payment(rs.getBoolean("payment"))
                    .isCompleted(rs.getBoolean("is_completed"))
                    .build();

            appointments.add(appointment);
        }
        return appointments;
    }

    public Appointment findAppointmentById(UUID appointmentId) throws SQLException {
        Type type = new TypeToken<Map<String, ArrayList<String>>>(){}.getType();

        PreparedStatement stmt = connection.prepareStatement("select * from appointments a\n" +
                "join doctors d on d.id = a.doctor_id\n" +
                "join users u on a.user_id = u.id\n" +
                "where a.id = ?");

        stmt.setObject(1, appointmentId);

        ResultSet rs = stmt.executeQuery();

        if(rs.next()) {

            return Appointment.builder()
                    .id(UUID.fromString(rs.getString("id")))
                    .user(User.builder()
                            .id(rs.getInt("user_id"))
                            .name(rs.getString("name"))
                            .build())
                    .doctor(Doctor.builder()
                            .id(rs.getInt("doctor_id"))
                            .speciality(rs.getString("speciality"))
                            .degree(rs.getString("degree"))
                            .fees(rs.getDouble("fees"))
                            .available(rs.getBoolean("available"))
                            .experience(rs.getString("experience"))
                            .about(rs.getString("about"))
                            .slotsBooked(gson.fromJson(rs.getString("slots_booked"), type))
                            .build())
                    .slotDate(rs.getString("slot_date"))
                    .slotTime(rs.getString("slot_time"))
                    .amount(rs.getDouble("amount"))
                    .date(rs.getString("date"))
                    .cancelled(rs.getBoolean("cancelled"))
                    .payment(rs.getBoolean("payment"))
                    .isCompleted(rs.getBoolean("is_completed"))
                    .build();
        }
        return null;
    }

    public boolean cancelAppointment(UUID appointmentId) throws SQLException {

        PreparedStatement stmt = connection.prepareStatement("UPDATE appointments SET cancelled = true WHERE id = ?");
        stmt.setObject(1, appointmentId);
        int rowsAffected = stmt.executeUpdate();

        return rowsAffected > 0;
    }

    public boolean updateAppointmentPaymentStatus(UUID appointmentId) throws SQLException {

        PreparedStatement stmt = connection.prepareStatement("UPDATE appointments SET payment = ? WHERE id = ?");
        stmt.setBoolean(1, true);
        stmt.setObject(2, appointmentId);
        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;

    }
}
