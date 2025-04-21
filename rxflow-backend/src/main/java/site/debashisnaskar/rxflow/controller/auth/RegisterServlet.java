package site.debashisnaskar.rxflow.controller.auth;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.debashisnaskar.rxflow.dto.RegisterRequest;
import site.debashisnaskar.rxflow.service.AuthService;
import site.debashisnaskar.rxflow.utils.Utils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.Logger;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(RegisterServlet.class.getName());
    private static final Gson gson = Utils.getGsonInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        AuthService authService = Utils.getAuthServiceInstance();
        resp.setContentType("application/json");

        String baseURL = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath();
        String defaultImageUrl = baseURL + "/images/default.png";

        String requestBody = Utils.readJsonBody(req);
        RegisterRequest registerRequest = gson.fromJson(requestBody, RegisterRequest.class);

        if(registerRequest.getUsername() == null || registerRequest.getPassword() == null || registerRequest.getName() == null) {
            Utils.buildJsonResponse("Missing required fields (username , password, name)",false,resp,HttpServletResponse.SC_BAD_REQUEST);
            logger.severe("Missing required fields (username , password, name)" + registerRequest);
            return;
        }

        if(registerRequest.getPassword().length() < 8) {
            Utils.buildJsonResponse("Enter a strong Password",false,resp,HttpServletResponse.SC_BAD_REQUEST);
            logger.severe("Enter a strong Password" + registerRequest);
            return;
        }

        try{
            authService.register(registerRequest,defaultImageUrl);

            Utils.buildJsonResponse("User successfully registered",true,resp,HttpServletResponse.SC_CREATED);
            logger.info("User registered successfully with " + registerRequest.getUsername() + " at " + LocalDateTime.now());
        }catch (ClassNotFoundException e) {
            Utils.buildJsonResponse(e.getMessage(),false,resp,HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.info("User registered error at " + LocalDateTime.now() + " error : " + e.getMessage());
        }catch (Exception e) {
            Utils.buildJsonResponse(e.getMessage(),false,resp,HttpServletResponse.SC_BAD_REQUEST);
            logger.info("User registered error at " + LocalDateTime.now() + " error : " + e.getMessage());
        }
    }


}
