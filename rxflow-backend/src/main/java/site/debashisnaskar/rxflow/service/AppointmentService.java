package site.debashisnaskar.rxflow.service;

import site.debashisnaskar.rxflow.dto.BookAppointmentRequest;
import site.debashisnaskar.rxflow.model.Appointment;
import site.debashisnaskar.rxflow.model.Doctor;
import site.debashisnaskar.rxflow.model.User;
import site.debashisnaskar.rxflow.repository.AppointmentRepository;
import site.debashisnaskar.rxflow.repository.DoctorRepository;
import site.debashisnaskar.rxflow.repository.UserRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class AppointmentService {
    private static final AppointmentRepository appointmentRepository = new AppointmentRepository();
    private static final DoctorRepository doctorRepository = new DoctorRepository();
    private static final UserRepository userRepository = new UserRepository();

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
        List<Appointment> appointmentsByUser = appointmentRepository.getAppointmentsByUser(userId);

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

        if(!appointmentRepository.checkAppointment(appointment)) {
            throw new RuntimeException("Appointment already booked");
        }
        appointmentRepository.bookAppointment(appointment);

        doctorRepository.updateSlotBooked(doctorId,slotsBooked);

        return true;
    }

    public List<Appointment> getAppointmentsByUserId(int userId) throws SQLException {
        return appointmentRepository.getAppointmentsByUser(userId);
    }

    public boolean cancelAppointment(int userId, UUID appointmentId) throws SQLException {

        Appointment appointment = appointmentRepository.findAppointmentById(appointmentId);

        if(appointment == null) {
            throw new RuntimeException("Appointment not found");
        }

        if(userId != appointment.getUser().getId()){
            throw new RuntimeException("User id mismatch : can't cancel appointment");
        }

        boolean isCancelled = appointmentRepository.cancelAppointment(appointmentId);

        Map<String, ArrayList<String>> slotsBooked = appointment.getDoctor().getSlotsBooked();

        ArrayList<String> slotsBookedByDate = slotsBooked.get(appointment.getSlotDate());
        slotsBookedByDate.remove(appointment.getSlotTime());

        slotsBooked.put(appointment.getSlotDate(),slotsBookedByDate);

        boolean isUpdated = doctorRepository.updateSlotBooked(appointment.getDoctor().getId(), slotsBooked);


        return isCancelled && isUpdated;
    }

}
