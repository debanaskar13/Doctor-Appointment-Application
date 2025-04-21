package site.debashisnaskar.rxflow.controller.admin;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import site.debashisnaskar.rxflow.exception.UserNotFoundException;
import site.debashisnaskar.rxflow.model.Doctor;
import site.debashisnaskar.rxflow.model.User;
import site.debashisnaskar.rxflow.service.DoctorService;
import site.debashisnaskar.rxflow.utils.Utils;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.logging.Logger;

@WebServlet("/admin/doctors")
@MultipartConfig
public class AddAndFetchAllDoctorsServlet extends HttpServlet {

    private static final DoctorService doctorService = new DoctorService();
    private static final Gson gson = Utils.getGsonInstance();
    private static final Logger logger = Logger.getLogger(AddAndFetchAllDoctorsServlet.class.getName());

//    Fetch All Doctors
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
//        if(!isAuthorized(req,resp)){
//            return;
//        }
        User user = (User) req.getAttribute("user");
        try{
            List<Doctor> allDoctors = doctorService.getAllDoctors();

            String responseJson = gson.toJson(allDoctors);
            resp.getWriter().write("{\"success\":true,\"doctors\":"+responseJson+"}");
            logger.info("Fetching all doctors by user : " + (user != null ? user.getUsername() : "Guest"));
        }catch (Exception e) {
            Utils.buildJsonResponse(e.getMessage(),false,resp,HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.severe("Error fetching all doctors by user : " + (user != null ? user.getUsername() : "Guest") + "error : " + e.getMessage());
        }
    }

//    Add Doctor
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        if(!isAuthorized(req,resp)){
            return;
        }
        User user = (User) req.getAttribute("user");

        String contextPath = req.getContextPath();
        String defaultImageUrl = contextPath + "/images/default.png";
        Part filePart = req.getPart("image");
        String requestBody = req.getParameter("doctorData");

        try {

            Gson gson = Utils.getGsonInstance();
            Doctor doctor = gson.fromJson(requestBody, Doctor.class);
            doctorService.addDoctor(doctor,filePart,defaultImageUrl);
            Utils.buildJsonResponse("Doctor added successfully",true,resp,HttpServletResponse.SC_CREATED);
            logger.info("Doctor added successfully by user : " + user.getUsername());
        }catch (UserNotFoundException e){
            Utils.buildJsonResponse("User not found",false,resp,HttpServletResponse.SC_NOT_FOUND);
            logger.severe("User not found, user : " + user.getUsername());
        }catch (IOException | JsonSyntaxException e) {
            Utils.buildJsonResponse(e.getMessage(),false,resp,HttpServletResponse.SC_BAD_REQUEST);
            logger.severe("Error creating doctor "+ requestBody + " by user : " + user.getUsername() + " error : " + e.getMessage());
        }catch (SQLIntegrityConstraintViolationException e){
            Utils.buildJsonResponse("doctor with the same user id is already available",false,resp,HttpServletResponse.SC_CONFLICT);
            logger.severe("Error creating doctor "+ requestBody + " by user : " + user.getUsername() + " error : " + e.getMessage());
        }catch (Exception e) {
            Utils.buildJsonResponse(e.getMessage(),false,resp,HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.severe("Error creating doctor "+ requestBody + " by user : " + user.getUsername() + " error : " + e.getMessage());
        }

    }

    private boolean isAuthorized(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getAttribute("user");
        if (user == null) {
            Utils.buildJsonResponse("Please login to access this page",false,resp,HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        if(!user.getRole().equals("ROLE_ADMIN")) {
            Utils.buildJsonResponse("You don't have access to this page",false,resp,HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        return true;
    }
}
