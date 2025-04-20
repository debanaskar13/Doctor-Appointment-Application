package site.debashisnaskar.rxflow.controller.auth;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.debashisnaskar.rxflow.dto.LoginRequest;
import site.debashisnaskar.rxflow.dto.LoginResponse;
import site.debashisnaskar.rxflow.service.AuthService;
import site.debashisnaskar.rxflow.utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;
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
        String jsonResponse = "";

        try{

            LoginResponse loginResponse = authService.login(loginRequest);
            jsonResponse= gson.toJson(loginResponse);

            if(loginResponse != null) {
                writer.write(jsonResponse);
                logger.info("User login successfully with " + loginRequest.getUsername() + " at " + LocalDateTime.now());
            }else{
                Utils.buildJsonResponse("Invalid username or password",false,resp,HttpServletResponse.SC_UNAUTHORIZED);
                logger.severe("Invalid Username or Password --> username : " + loginRequest.getUsername() );
            }

        } catch (Exception e) {
            Utils.buildJsonResponse("Something went wrong",false,resp,HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.severe("Something went wrong --> username : " + loginRequest.getUsername() + " error : " + e );
        } finally {
            writer.flush();
            writer.close();
        }
    }
}
