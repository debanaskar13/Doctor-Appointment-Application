package site.debashisnaskar.rxflow.controller.user;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.debashisnaskar.rxflow.model.User;
import site.debashisnaskar.rxflow.service.UserService;
import site.debashisnaskar.rxflow.utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/users")
public class UserServlet extends HttpServlet {

    private final UserService userService = new UserService();
    private final Gson gson = Utils.getGsonInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        User user = (User) req.getAttribute("user");
        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        if(!user.getRole().equals("ROLE_ADMIN")) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Gson gson = new Gson();
        String responseJson = gson.toJson(userService.findAllUsers());
        writer.write(responseJson);
    }
}
