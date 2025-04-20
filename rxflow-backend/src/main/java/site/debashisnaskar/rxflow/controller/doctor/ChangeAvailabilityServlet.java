package site.debashisnaskar.rxflow.controller.doctor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.debashisnaskar.rxflow.model.User;
import site.debashisnaskar.rxflow.service.DoctorService;
import site.debashisnaskar.rxflow.utils.Utils;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

@WebServlet("/doctors/change-availability")
public class ChangeAvailabilityServlet extends HttpServlet {
    private static final DoctorService doctorService = new DoctorService();
    private static final Logger logger = Logger.getLogger(ChangeAvailabilityServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        if(!isAuthorized(req,resp)){
            return;
        }
        try {
            String jsonBody = Utils.readJsonBody(req);

            JsonObject requestBody = JsonParser.parseString(jsonBody).getAsJsonObject();
            String doctorId = requestBody.get("doctorId").toString();

            boolean isUpdated = doctorService.changeAvailability(Integer.parseInt(doctorId));
            if (isUpdated) {
                Utils.buildJsonResponse("Availability changed",true,resp,HttpServletResponse.SC_OK);
                logger.info("Doctor availability changed for doctor " + doctorId);
            }else {
                Utils.buildJsonResponse("Availability not changed",false,resp,HttpServletResponse.SC_BAD_REQUEST);
                logger.info("Doctor availability not changed for doctor " + doctorId);
            }

        } catch (Exception e) {
            Utils.buildJsonResponse(e.getMessage(),false,resp,HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.info(e.getMessage());
        }
    }

    private boolean isAuthorized(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getAttribute("user");
        if (user == null) {
            Utils.buildJsonResponse("Please login to access this page",false,resp,HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        if(!user.getRole().equals("ROLE_ADMIN") && !user.getRole().equals("ROLE_DOCTOR")) {
            Utils.buildJsonResponse("You don't have access to this page",false,resp,HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        return true;
    }
}
