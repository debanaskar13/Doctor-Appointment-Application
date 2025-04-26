package site.debashisnaskar.rxflow.controller.payment;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.debashisnaskar.rxflow.dto.VerifyPaymentRequest;
import site.debashisnaskar.rxflow.service.PaymentService;
import site.debashisnaskar.rxflow.utils.Utils;

import java.io.IOException;
import java.util.logging.Logger;


@WebServlet("/user/payment/verify-payment")
public class VerifyPaymentServlet extends HttpServlet {

    private static final Gson gson = Utils.getGsonInstance();
    private static final Logger logger = Logger.getLogger(VerifyPaymentServlet.class.getName());
    private static final PaymentService paymentService = new PaymentService();


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String jsonRequestBody = Utils.readJsonBody(req);
        VerifyPaymentRequest verifyPaymentRequest = gson.fromJson(jsonRequestBody, VerifyPaymentRequest.class);

        try {
            boolean isVerified = paymentService.verifyPayment(verifyPaymentRequest);

            if(isVerified) {
                Utils.buildJsonResponse("Payment successful",true,resp,HttpServletResponse.SC_OK);
            }

        } catch (Exception e) {
            Utils.buildJsonResponse("Payment failed",false,resp,HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.severe(e.getMessage());
        }

    }
}
