package site.debashisnaskar.rxflow.service;

import com.google.gson.Gson;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONObject;
import site.debashisnaskar.rxflow.dto.VerifyPaymentRequest;
import site.debashisnaskar.rxflow.model.Appointment;
import site.debashisnaskar.rxflow.repository.AppointmentRepository;
import site.debashisnaskar.rxflow.utils.Utils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public class PaymentService {
    private static final AppointmentRepository appointmentRepository = new AppointmentRepository();
    private static final Dotenv dotenv = Dotenv.load();
    private static final Gson gson = Utils.getGsonInstance();
    private static final Logger logger = Logger.getLogger(PaymentService.class.getName());

    public Order paymentRazorPay(UUID appointmentId) throws SQLException {
        Appointment appointment = appointmentRepository.findAppointmentById(appointmentId);

        if(appointment == null || appointment.isCancelled()) {
            throw new RuntimeException("Appointment Cancelled or not found");
        }

        String razorPaySecret = dotenv.get("RAZOR_PAY_SECRET");
        String razorPayApiKey = dotenv.get("RAZOR_PAY_API_KEY");
        String razorPayCurrency = dotenv.get("RAZOR_PAY_CURRENCY");

        try {
            RazorpayClient razorpayClient = new RazorpayClient(razorPayApiKey, razorPaySecret);
            JSONObject options = new JSONObject();
            options.put("amount", appointment.getAmount() * 100);
            options.put("currency", razorPayCurrency);
            options.put("receipt", appointment.getId());

            return razorpayClient.orders.create(options);
        } catch (RazorpayException e) {
            throw new RuntimeException("Order Creation Failed");
        }

    }

    public boolean verifyPayment(VerifyPaymentRequest verifyPaymentRequest) {

        HashMap<String, String> options = new HashMap<>();
        options.put("razorpay_order_id", verifyPaymentRequest.getRazorpayOrderId());
        options.put("razorpay_payment_id", verifyPaymentRequest.getRazorpayPaymentId());
        options.put("razorpay_signature", verifyPaymentRequest.getRazorpaySignature());

        try {
            RazorpayClient razorpayClient = new RazorpayClient(dotenv.get("RAZOR_PAY_API_KEY"), dotenv.get("RAZOR_PAY_SECRET"));

            Order orderInfo = razorpayClient.orders.fetch(verifyPaymentRequest.getRazorpayOrderId());
            boolean isValid = com.razorpay.Utils.verifyPaymentSignature(new JSONObject(options), dotenv.get("RAZOR_PAY_SECRET"));

            if(isValid){
                UUID appointmentId = UUID.fromString(orderInfo.get("receipt"));
                return appointmentRepository.updateAppointmentPaymentStatus(appointmentId);
            }

        } catch (RazorpayException e) {
            logger.severe(e.getMessage());
            throw new RuntimeException("Razor Pay Failed");
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            throw new RuntimeException("Unable to update appointment payment");
        }

        return false;
    }
}
