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
import java.util.regex.Pattern;

@WebServlet("/doctors/*")
public class UpdateDeleteAndFetchDoctorServlet extends HttpServlet {

    private static final Pattern pattern = Pattern.compile("^/([a-zA-Z0-9]+)/?$");
    private static final DoctorService doctorService = new DoctorService();
    private static final Gson gson = Utils.getGsonInstance();

//    Fetch Specific Doctor by ID
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String pathInfo = req.getPathInfo();
        String doctorId = Utils.extractPathVariable(pathInfo, pattern);

        if(doctorId != null) {
            try {
                Doctor doctor = doctorService.getDoctor(Integer.parseInt(doctorId));

                if(!isAuthorized(req, resp, doctor)){
                    return;
                }

                if(doctor == null) {
                    resp.setStatus(404);
                    resp.getWriter().write("\"success\": \"false\", \"message\": \"doctor id " + doctorId + " not found\"");
                    return;
                }

                String responseJson = gson.toJson(doctor);
                resp.getWriter().write(responseJson);

            }catch (DoctorNotFoundException e) {
                resp.setStatus(404);
                resp.getWriter().write("\"success\": \"false\", \"message\": \"doctor not found\"");
            }catch(NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("\"success\": \"error\", \"message\": \"invalid doctor id\"\" ");
            } catch (SQLException | ClassNotFoundException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("\"success\": \"error\", \"message\": \"something wrong on server\"\" ");
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("\"success\": \"error\", \"message\": \"something went wrong\"\" ");
            }
        }

    }

//    Update Specific Doctor by ID
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String pathInfo = req.getPathInfo();
        String doctorId = Utils.extractPathVariable(pathInfo, pattern);

        if(doctorId != null) {
            Doctor doctor = null;
            try {
                doctor = doctorService.getDoctor(Integer.parseInt(doctorId));
                if(!isAuthorized(req, resp, doctor)){
                    return;
                }

            } catch (DoctorNotFoundException e) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("\"success\": \"false\", \"message\": \"doctor not found\"");
            } catch (SQLException | ClassNotFoundException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("\"success\": \"false\", \"message\": \"something went wrong\"");
            }
//            Update Doctor here
            resp.getWriter().write("\"doctorId\": \"" + doctorId + "\" ");
        }

    }

//    Delete Specific Doctor by ID
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String pathInfo = req.getPathInfo();
        String doctorId = Utils.extractPathVariable(pathInfo, pattern);

        if(doctorId != null) {
            try {
                Doctor doctor = doctorService.getDoctor(Integer.parseInt(doctorId));
                if(!isAuthorized(req, resp, doctor)){
                    return;
                }

                boolean isDocDeleted = doctorService.deleteDoctor(Integer.parseInt(doctorId));
                if(isDocDeleted) {
                    resp.setStatus(200);
                    resp.getWriter().write("\"success\": \"true\", \"message\": \" " + doctorId + "\" deleted successfully\"");
                    return;
                }
                resp.setStatus(404);
                resp.getWriter().write("\"success\": \"false\", \"message\": \"" + doctorId + "\" not found\"");
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }catch (NumberFormatException e){
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("\"success\":\"error\", \"message\":\"invalid doctor id\"");
            } catch (DoctorNotFoundException e) {
                throw new RuntimeException(e);
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
