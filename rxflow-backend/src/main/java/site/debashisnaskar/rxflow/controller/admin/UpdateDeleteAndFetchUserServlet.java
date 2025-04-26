package site.debashisnaskar.rxflow.controller.admin;

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
import java.util.regex.Pattern;

@WebServlet("/api/admin/users/*")
public class UpdateDeleteAndFetchUserServlet extends HttpServlet {

    private static final Pattern pattern = Pattern.compile("^/([a-zA-Z0-9]+)/?$");
    private static final UserService userService = new UserService();
    private static final Gson gson = Utils.getGsonInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
//        String pathInfo = req.getPathInfo();
//        String userId = Utils.extractPathVariable(pathInfo, pattern);
        String userId = req.getAttribute("userId").toString();
        if(userId != null) {

            try {
                User user = userService.findUserById(Integer.parseInt(userId));
                if(user == null) {
                    resp.setStatus(404);
                    resp.getWriter().write("\"success\": \"false\", \"message\": \"user not found\"");
                    return;
                }
                String responseJson = Utils.getGsonInstance().toJson(user);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(responseJson);
            }catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("\"success\": \"error\", \"message\": \"invalid user id\"");
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        String userId = req.getAttribute("userId").toString();
        String requestBodyJson = Utils.readJsonBody(req);

        User user = gson.fromJson(requestBodyJson, User.class);

        if(userId != null) {
            try {
                user.setId(Integer.parseInt(userId));
                boolean isUpdated = userService.updateUser(user);
                if(isUpdated) {
                    Utils.buildJsonResponse("User updated successfully",true,resp,HttpServletResponse.SC_OK);
                }else{
                    Utils.buildJsonResponse("User update failed",false,resp,HttpServletResponse.SC_BAD_REQUEST);
                }
            } catch (Exception e) {
                Utils.buildJsonResponse(e.getMessage(),false,resp,HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }



    }
}
