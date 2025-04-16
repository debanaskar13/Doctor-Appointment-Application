package site.debashisnaskar.rxflow.controller.doctor;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.debashisnaskar.rxflow.exception.UserNotFoundException;
import site.debashisnaskar.rxflow.model.Doctor;
import site.debashisnaskar.rxflow.model.User;
import site.debashisnaskar.rxflow.service.DoctorService;
import site.debashisnaskar.rxflow.utils.Utils;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@WebServlet("/doctors")
public class AddAndFetchAllDoctorsServlet extends HttpServlet {

    private static final DoctorService doctorService = new DoctorService();
    private static final Gson gson = Utils.getGsonInstance();

//    Fetch All Doctors
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        if(!isAuthorized(req,resp)){
            return;
        }

        try{
            List<Doctor> allDoctors = doctorService.getAllDoctors();

            String responseJson = gson.toJson(allDoctors);
            resp.getWriter().write(responseJson);
        }catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

//    Add Doctor
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        if(!isAuthorized(req,resp)){
            return;
        }

        try {
            String requestBody = Utils.readJsonBody(req);

            Gson gson = new Gson();
            Doctor doctor = gson.fromJson(requestBody, Doctor.class);
            doctorService.addDoctor(doctor);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("{\"success\": true,\"message\": \"Doctor added successfully\"}");
        }catch (UserNotFoundException e){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"success\": false,\"message\": \"User not found\"}");
        }catch (IOException | JsonSyntaxException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("{\"success\": false , \"message\": \"" + e.getMessage() + "\"");
        }catch (SQLIntegrityConstraintViolationException e){
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write("{\"success\": false, \"message\": \"doctor with the same user id is already available\"}\"");
        }catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("{\"success\": false , \"message\": \"" + e.getMessage() + "\"");
        }

    }

    private boolean isAuthorized(HttpServletRequest req, HttpServletResponse resp) {
        User user = (User) req.getAttribute("user");
        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        if(!user.getRole().equals("ROLE_ADMIN")) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        return true;
    }
}
