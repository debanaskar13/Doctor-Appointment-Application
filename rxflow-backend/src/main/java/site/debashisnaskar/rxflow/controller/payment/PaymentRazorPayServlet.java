package site.debashisnaskar.rxflow.controller.payment;

import com.google.gson.Gson;
import com.razorpay.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.debashisnaskar.rxflow.model.Appointment;
import site.debashisnaskar.rxflow.model.User;
import site.debashisnaskar.rxflow.service.PaymentService;
import site.debashisnaskar.rxflow.utils.Utils;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/user/payment/payment-razorpay")
public class PaymentRazorPayServlet extends HttpServlet {

    private static final PaymentService paymentService = new PaymentService();
    private static final Logger logger = Logger.getLogger(PaymentRazorPayServlet.class.getName());
    private static final Gson gson = Utils.getGsonInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        User loggedInUser = (User) req.getAttribute("user");


        try{
            String jsonRequestBody = Utils.readJsonBody(req);
            Appointment appointment = gson.fromJson(jsonRequestBody, Appointment.class);

            Order order = paymentService.paymentRazorPay(appointment.getId());
            String responseBody = gson.toJson(order);

            resp.getWriter().write("{\"success\":true, \"order\": " + responseBody + "}");
            logger.info("Payment initiated by user id : " + loggedInUser.getId());
        } catch (Exception e) {
            Utils.buildJsonResponse(e.getMessage(),false,resp,HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.severe(e.getMessage());
        }


    }
}
