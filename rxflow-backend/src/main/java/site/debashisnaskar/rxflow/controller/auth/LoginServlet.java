package site.debashisnaskar.rxflow.controller.auth;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.debashisnaskar.rxflow.utils.Utils;
import site.debashisnaskar.rxflow.dto.LoginRequest;
import site.debashisnaskar.rxflow.service.AuthService;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Logger;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        AuthService authService = Utils.getAuthServiceInstance();
        resp.setContentType("application/json");

        String requestBody = Utils.readJsonBody(req);

        Gson gson = Utils.getGsonInstance();
        LoginRequest loginRequest = gson.fromJson(requestBody, LoginRequest.class);
        PrintWriter writer = resp.getWriter();

        try{

            String token = authService.login(loginRequest);

            if(token != null) {
                writer.print("{\"success\": true,\"token\":\"" + token + "\"}");
                logger.info("User login successfully with " + loginRequest.getUsername() + " at " + LocalDateTime.now());
            }else{
                writer.print("{\"success\": false, \"message\":\"Invalid Username or Password\"}\"}");
                logger.severe("Invalid Username or Password --> username : " + loginRequest.getUsername() );
            }

        } catch (SQLException | ClassNotFoundException e) {
            logger.severe("Something went wrong --> username : " + loginRequest.getUsername() + " error : " + e);
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid username or password");
        }catch (Exception e) {
            logger.severe("Something went wrong --> username : " + loginRequest.getUsername() + " error : " + e );
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong");
        }
        writer.close();
    }
}
