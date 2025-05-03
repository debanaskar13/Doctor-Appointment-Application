package site.debashisnaskar.rxflow.controller.admin;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.debashisnaskar.rxflow.dto.Page;
import site.debashisnaskar.rxflow.model.User;
import site.debashisnaskar.rxflow.service.UserService;
import site.debashisnaskar.rxflow.utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/admin/users")
public class AddAndFetchAllUsersServlet extends HttpServlet {

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

        String page = req.getParameter("page") == null ? "1" : req.getParameter("page");
        String limit = req.getParameter("limit") == null ? "10" : req.getParameter("limit");
        String sortBy = req.getParameter("sortBy")  == null ? "id" : req.getParameter("sortBy");
        String direction = req.getParameter("direction") == null ? "ASC" : req.getParameter("direction");

        try{
            Page<List<User>> pageUser = new Page<>();

            pageUser.setPageNumber(Long.parseLong(page));
            pageUser.setPageSize(Long.parseLong(limit));
            pageUser.setSortBy(sortBy);
            pageUser.setSorDirection(direction);

            Page<List<User>> allUsers = userService.findAllUsers(pageUser);
            Gson gson = new Gson();
            String responseJson = gson.toJson(allUsers);
            writer.write("{\"success\": true, \"users\": " + responseJson + "}");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
