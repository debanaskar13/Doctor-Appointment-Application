package site.debashisnaskar.rxflow.service;

import site.debashisnaskar.rxflow.exception.DoctorNotFoundException;
import site.debashisnaskar.rxflow.exception.UserNotFoundException;
import site.debashisnaskar.rxflow.model.Doctor;
import site.debashisnaskar.rxflow.model.User;
import site.debashisnaskar.rxflow.repository.DoctorRepository;
import site.debashisnaskar.rxflow.repository.UserRepository;

import java.sql.SQLException;
import java.util.List;

public class DoctorService {

    private static final DoctorRepository doctorRepository = new DoctorRepository();
    private static final UserRepository userRepository = new UserRepository();

    public void addDoctor(Doctor doctor) throws SQLException, ClassNotFoundException, UserNotFoundException {

        int userId = doctor.getUser().getId();

        User user = userRepository.findById(userId);
        if(user != null) {
            doctor.setUser(user);
            doctorRepository.save(doctor);
        }else {
            throw new UserNotFoundException("user not found");
        }
    }

    public boolean deleteDoctor(int doctorId) throws SQLException, ClassNotFoundException {
        return doctorRepository.delete(doctorId);
    }

    public void updateDoctor(Doctor doctor) {

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
}
