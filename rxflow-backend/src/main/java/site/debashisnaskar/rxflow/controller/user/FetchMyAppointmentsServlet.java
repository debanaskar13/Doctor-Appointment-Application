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
import java.util.List;
import java.util.logging.Logger;

@WebServlet("/user/my-appointments")
public class FetchMyAppointmentsServlet extends HttpServlet {
    private static final UserService userService = new UserService();
    private static final Gson gson = Utils.getGsonInstance();
    private static final Logger logger = Logger.getLogger(FetchMyAppointmentsServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        User user = (User) req.getAttribute("user");

        try {
            List<Appointment> myAppointments = userService.getAppointmentsByUserId(user.getId());
            String responseBody = gson.toJson(myAppointments);

            resp.getWriter().write("{\"success\":true,\"appointments\":"+responseBody+"}");
            logger.info("My appointments fetched successfully");
        } catch (Exception e) {
            logger.severe(e.getMessage()+"\n"+e);
            Utils.buildJsonResponse(e.getMessage(),false,resp,HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }


    }
}
