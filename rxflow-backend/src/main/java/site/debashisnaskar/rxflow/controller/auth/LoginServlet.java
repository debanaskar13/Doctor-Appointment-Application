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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        AuthService authService = new AuthService();
        resp.setContentType("application/json");

        String requestBody = Utils.readJsonBody(req);

        Gson gson = new Gson();
        LoginRequest loginRequest = gson.fromJson(requestBody, LoginRequest.class);
        PrintWriter writer = resp.getWriter();

        try{

            String token = authService.login(loginRequest);

            if(token != null) {
                writer.print("{\"token\":\"" + token + "\"}");
            }else{
                writer.print("{\"error\":\"Something went wrong\"}\"}");
            }

        } catch (SQLException | ClassNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid username or password");
        }


    }


}
