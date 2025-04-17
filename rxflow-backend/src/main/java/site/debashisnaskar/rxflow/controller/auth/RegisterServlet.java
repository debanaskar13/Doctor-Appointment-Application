package site.debashisnaskar.rxflow.controller.auth;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.debashisnaskar.rxflow.utils.Utils;
import site.debashisnaskar.rxflow.dto.RegisterRequest;
import site.debashisnaskar.rxflow.service.AuthService;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Logger;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(RegisterServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        AuthService authService = Utils.getAuthServiceInstance();
        resp.setContentType("application/json");

        String contextPath = req.getContextPath();
        String defaultImageUrl = contextPath + "/images/default.png";

        String requestBody = Utils.readJsonBody(req);

        Gson gson = Utils.getGsonInstance();
        RegisterRequest registerRequest = gson.fromJson(requestBody, RegisterRequest.class);
        PrintWriter writer = resp.getWriter();

        try{
            authService.register(registerRequest,defaultImageUrl);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            writer.print("{\"status\":\"success\",\"message\":\"User successfully registered\"}");
            logger.info("User registered successfully with " + registerRequest.getUsername() + " at " + LocalDateTime.now());
        }catch (ClassNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writer.print("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
            logger.info("User registered error at " + LocalDateTime.now() + " error : " + e.getMessage());
        }catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writer.print("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
            logger.info("User registered error at " + LocalDateTime.now() + " error : " + e.getMessage());
        }
        writer.close();
    }
}
