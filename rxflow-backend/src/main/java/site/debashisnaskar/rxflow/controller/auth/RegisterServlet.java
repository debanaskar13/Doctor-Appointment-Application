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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        AuthService authService = Utils.getAuthServiceInstance();
        resp.setContentType("application/json");

        String contextPath = req.getContextPath();
        String defaultImageUrl = contextPath + "/images/default.png";

        String requestBody = Utils.readJsonBody(req);

        Gson gson = new Gson();
        RegisterRequest registerRequest = gson.fromJson(requestBody, RegisterRequest.class);
        PrintWriter writer = resp.getWriter();

        try{
            authService.register(registerRequest,defaultImageUrl);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            writer.print("{\"status\":\"success\",\"message\":\"User successfully registered\"}");
        }catch (ClassNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writer.print("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writer.print("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
        writer.close();
    }
}
