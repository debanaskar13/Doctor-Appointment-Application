package site.debashisnaskar.rxflow.controller.doctor;

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

@WebServlet("/doctors/*")
public class UpdateDeleteAndFetchDoctorServlet extends HttpServlet {

    private static final Pattern pattern = Pattern.compile("^/([a-zA-Z0-9]+)/?$");
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
                    resp.setStatus(404);
                    resp.getWriter().write("{\"success\": false, \"message\": \"doctor id " + doctorId + " not found\"}");
                    logger.severe("Error getting doctor id : " + doctorId + " by user : " + user.getUsername());
                    return;
                }

                String responseJson = gson.toJson(doctor);
                resp.getWriter().write(responseJson);

            }catch (DoctorNotFoundException e) {
                resp.setStatus(404);
                resp.getWriter().write("{\"success\": false, \"message\": \"doctor not found\"}");
                logger.severe("Error getting doctor id : " + doctorId + " by user : " + user.getUsername());
            }catch(NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"success\": false, \"message\": \"invalid doctor id\"}");
                logger.severe("Error getting doctor id : " + doctorId + " by user : " + user.getUsername());
            } catch (SQLException | ClassNotFoundException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"success\": false, \"message\": \"something wrong on server\"}");
                logger.severe("Error getting doctor id : " + doctorId + " by user : " + user.getUsername() + " error : " + e.getMessage());
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"success\": false, \"message\": \"something went wrong\"}");
                logger.severe("Error getting doctor id : " + doctorId + " by user : " + user.getUsername() + " error : " + e.getMessage());
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
                resp.getWriter().write("{\"success\": true , \"message\": \"doctor updated successfully\"}");
                logger.info("doctor updated successfully with doctor id : " + doctorId + " by user : " + user.getUsername());

            } catch (DoctorNotFoundException e) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"success\": false, \"message\": \"doctor not found\"}");
                logger.severe("Error getting doctor id : " + doctorId + " by user : " + user.getUsername());
            } catch (SQLException | ClassNotFoundException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"success\": false, \"message\": \"something went wrong\"}");
                logger.severe("Error updating doctor with id : " + doctorId + " by user : " + user.getUsername() + " error : " + e.getMessage());
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
                    resp.setStatus(200);
                    resp.getWriter().write("{\"success\": true, \"message\": \" " + doctorId + " deleted successfully\"}");
                    logger.info("Doctor deleted successfully with doctor id : " + doctorId + " by user : " + user.getUsername());
                    return;
                }
                resp.setStatus(404);
                resp.getWriter().write("{\"success\": false, \"message\": \"" + doctorId + "not found\"}");
                logger.severe("Error deleting doctor id : " + doctorId + " by user : " + user.getUsername());
            } catch (SQLException | ClassNotFoundException e) {
                resp.setStatus(404);
                resp.getWriter().write("{\"success\": false, \"message\": \"something went wrong\"}");
                logger.severe("Error deleting doctor id : " + doctorId + " by user : " + user.getUsername() + " error : " + e.getMessage());
            }catch (NumberFormatException e){
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"success\": false, \"message\":\"invalid doctor id\"}");
                logger.severe("Error deleting doctor id : " + doctorId + " by user : " + user.getUsername() + " error : " + e.getMessage());
            } catch (DoctorNotFoundException e) {
                resp.setStatus(404);
                resp.getWriter().write("{\"success\": false, \"message\": \"" + doctorId + " not found\"}");
                logger.severe("Error deleting doctor id : " + doctorId + " by user : " + user.getUsername() + " error : " + e.getMessage());
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
