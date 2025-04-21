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
import java.sql.SQLException;

@WebServlet("/user/profile")
public class getUserProfileServlet extends HttpServlet {
    private static final UserService userService = new UserService();
    private static final Gson gson = Utils.getGsonInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        User user = (User) req.getAttribute("user");

        try {
            User userProfile = userService.getUserProfile(user);

            if(userProfile != null) {
                resp.getWriter().print("{\"success\":true,\"userProfile\":" + gson.toJson(userProfile) + "}");
                resp.setStatus(HttpServletResponse.SC_OK);
            }else{
                Utils.buildJsonResponse("invalid user",false,resp,HttpServletResponse.SC_UNAUTHORIZED);
            }

        } catch (SQLException e) {
            Utils.buildJsonResponse(e.getMessage(),false,resp,HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }
}
