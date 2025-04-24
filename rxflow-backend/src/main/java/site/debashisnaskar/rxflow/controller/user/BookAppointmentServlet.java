package site.debashisnaskar.rxflow.controller.user;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.debashisnaskar.rxflow.dto.BookAppointmentRequest;
import site.debashisnaskar.rxflow.model.User;
import site.debashisnaskar.rxflow.service.UserService;
import site.debashisnaskar.rxflow.utils.Utils;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/user/book-appointment")
public class BookAppointmentServlet extends HttpServlet {

    private static final Gson gson = Utils.getGsonInstance();
    private static final UserService userService = new UserService();
    private static final Logger logger = Logger.getLogger(BookAppointmentServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String requestBody = Utils.readJsonBody(req);
        User user = (User) req.getAttribute("user");

        BookAppointmentRequest appointmentRequest = gson.fromJson(requestBody, BookAppointmentRequest.class);
        appointmentRequest.setUserId(user.getId());

        try {
            userService.bookAppointment(appointmentRequest);
            Utils.buildJsonResponse("Appointment Booked",true,resp,HttpServletResponse.SC_OK);
            logger.info("Appointment Booked by userId : " + appointmentRequest.getUserId() + " with doctorID : " + appointmentRequest.getDoctorId());
        } catch (Exception e) {
            Utils.buildJsonResponse("Appointment Booking Failed : " + e.getMessage(),false,resp,HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.severe(e.getMessage());
        }

    }
}
