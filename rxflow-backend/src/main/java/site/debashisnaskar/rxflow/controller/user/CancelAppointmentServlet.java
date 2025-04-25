package site.debashisnaskar.rxflow.controller.user;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.debashisnaskar.rxflow.model.Appointment;
import site.debashisnaskar.rxflow.model.User;
import site.debashisnaskar.rxflow.service.UserService;
import site.debashisnaskar.rxflow.utils.Utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

@WebServlet("/user/cancel-appointment")
public class CancelAppointmentServlet extends HttpServlet {

    private static final UserService userService = new UserService();
    private static final Gson gson = Utils.getGsonInstance();
    private static final Logger logger = Logger.getLogger(CancelAppointmentServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        String requestBody = Utils.readJsonBody(req);
        User user = (User) req.getAttribute("user");


        try {
            Appointment appointmentRequest = gson.fromJson(requestBody, Appointment.class);
            if(appointmentRequest == null) {
                Utils.buildJsonResponse("invalid appointment id",false,resp,HttpServletResponse.SC_BAD_REQUEST);
                logger.severe("invalid appointment id");
                return;
            }
            boolean isCancelled = userService.cancelAppointment(user.getId(), appointmentRequest.getId());
            if(isCancelled) {
                Utils.buildJsonResponse("Appointment Cancelled",true,resp,HttpServletResponse.SC_OK);
                logger.info("Appointment Cancelled by user : " + user.getId() + " for appointment : " + appointmentRequest.getId());
            }else{
                Utils.buildJsonResponse("Appointment Cancellation Failed",false,resp,HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                logger.severe("Appointment Cancellation Failed");
            }
        } catch (Exception e) {
            Utils.buildJsonResponse(e.getMessage(),false,resp,HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.severe("Appointment Cancellation Failed , error : " + e.getMessage());
        }
    }
}
