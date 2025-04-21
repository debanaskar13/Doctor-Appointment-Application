package site.debashisnaskar.rxflow.controller.admin;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.debashisnaskar.rxflow.exception.DoctorNotFoundException;
import site.debashisnaskar.rxflow.model.Doctor;
import site.debashisnaskar.rxflow.model.User;
import site.debashisnaskar.rxflow.service.DoctorService;
import site.debashisnaskar.rxflow.utils.Utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@WebServlet("/admin/doctors/*")
public class UpdateDeleteAndFetchDoctorServlet extends HttpServlet {

    private static final Pattern pattern = Pattern.compile("^/admin/doctors/([a-zA-Z0-9]+)/?$");
    private static final DoctorService doctorService = new DoctorService();
    private static final Gson gson = Utils.getGsonInstance();
    private  static final Logger logger = Logger.getLogger(UpdateDeleteAndFetchDoctorServlet.class.getName());

//    Fetch Specific Doctor by ID
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String pathInfo = req.getPathInfo();
        String doctorId = Utils.extractPathVariable(pathInfo, pattern);
        if(doctorId != null) {
            User user = null;
            try {
                Doctor doctor = doctorService.getDoctor(Integer.parseInt(doctorId));

                if(!isAuthorized(req, resp, doctor)){
                    return;
                }
                user = (User) req.getAttribute("user");

                if(doctor == null) {
                    Utils.buildJsonResponse("doctor id " + doctorId + " not found",false,resp,HttpServletResponse.SC_NOT_FOUND);
                    logger.severe("Error getting doctor id : " + doctorId + " by user : " + user.getUsername());
                    return;
                }

                String responseJson = gson.toJson(doctor);
                resp.getWriter().write(responseJson);

            }catch (DoctorNotFoundException e) {
                Utils.buildJsonResponse("doctor not found",false,resp,HttpServletResponse.SC_NOT_FOUND);
                logger.severe("Error getting doctor id : " + doctorId + " by user : " + user);
            }catch(NumberFormatException e) {
                Utils.buildJsonResponse("invalid doctor id",false,resp,HttpServletResponse.SC_CONFLICT);
                logger.severe("Error getting doctor id : " + doctorId + " by user : " + user);
            } catch (Exception e) {
                Utils.buildJsonResponse("something wrong on server",false,resp,HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                logger.severe("Error getting doctor id : " + doctorId + " by user : " + user + " error : " + e.getMessage());
            }
        }

    }

//    Update Specific Doctor by ID
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String pathInfo = req.getPathInfo();
        String doctorId = Utils.extractPathVariable(pathInfo, pattern);

        String requestBody = Utils.readJsonBody(req);
        Doctor doctorRequested = gson.fromJson(requestBody, Doctor.class);
        if(doctorId != null) {
            User user = null;
            try {
                Doctor doctor = doctorService.getDoctor(Integer.parseInt(doctorId));
                if(!isAuthorized(req, resp, doctor)){
                    return;
                }
                user = (User) req.getAttribute("user");

//                Update Doctor here
                doctorService.updateDoctor(doctorRequested);
                Utils.buildJsonResponse("doctor updated successfully",true,resp,HttpServletResponse.SC_OK);
                logger.info("doctor updated successfully with doctor id : " + doctorId + " by user : " + user.getUsername());

            } catch (DoctorNotFoundException e) {
                Utils.buildJsonResponse("doctor not found",false,resp,HttpServletResponse.SC_NOT_FOUND);
                logger.severe("Error getting doctor id : " + doctorId + " by user : " + user);
            } catch (SQLException | ClassNotFoundException e) {
                Utils.buildJsonResponse("something went wrong",false,resp,HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                logger.severe("Error updating doctor with id : " + doctorId + " by user : " + user + " error : " + e.getMessage());
            }
        }

    }

//    Delete Specific Doctor by ID
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String pathInfo = req.getPathInfo();
        String doctorId = Utils.extractPathVariable(pathInfo, pattern);

        if(doctorId != null) {
            User user = null;
            try {
                Doctor doctor = doctorService.getDoctor(Integer.parseInt(doctorId));
                if(!isAuthorized(req, resp, doctor)){
                    return;
                }
                user = (User) req.getAttribute("user");

                boolean isDocDeleted = doctorService.deleteDoctor(Integer.parseInt(doctorId));
                if(isDocDeleted) {
                    Utils.buildJsonResponse("doctor deleted successfully",true,resp,HttpServletResponse.SC_OK);
                    logger.info("Doctor deleted successfully with doctor id : " + doctorId + " by user : " + user.getUsername());
                    return;
                }

                Utils.buildJsonResponse("doctor with id " + doctorId + " not found",false,resp,HttpServletResponse.SC_NOT_FOUND);
                logger.severe("Error deleting doctor id : " + doctorId + " by user : " + user.getUsername());
            } catch (NumberFormatException e){
                Utils.buildJsonResponse("invalid doctor id",false,resp,HttpServletResponse.SC_BAD_REQUEST);
                logger.severe("Error deleting doctor id : " + doctorId + " by user : " + user + " error : " + e.getMessage());
            } catch (DoctorNotFoundException e) {
                Utils.buildJsonResponse("doctor with id " + doctorId + " not found",false,resp,HttpServletResponse.SC_NOT_FOUND);
                logger.severe("Error deleting doctor id : " + doctorId + " by user : " + user + " error : " + e.getMessage());
            } catch (Exception e){
                Utils.buildJsonResponse("something went wrong",false,resp,HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                logger.severe("Error deleting doctor id : " + doctorId + " by user : " + user + " error : " + e.getMessage());
            }
        }
    }

    private boolean isAuthorized(HttpServletRequest req, HttpServletResponse resp, Doctor doctor) {
        User user = (User) req.getAttribute("user");
        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

//        if doctor is not null and user is not ADMIN or doctor's user id is not equal to current login user id then send Forbidden

        if( doctor != null && !user.getRole().equals("ROLE_ADMIN")) {
            if(doctor.getUser().getId() != user.getId()){
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return false;
            }
        }
        return true;
    }

}
