package site.debashisnaskar.rxflow.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.debashisnaskar.rxflow.model.User;
import site.debashisnaskar.rxflow.utils.Utils;

import java.io.IOException;


// ******************************* FRONT ADMIN CONTROLLER **************************************************

@WebServlet("/admin/*")
@MultipartConfig
public class AdminFrontController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        User user = (User) req.getAttribute("user");

        if(!user.getRole().equals("ROLE_ADMIN")) {
            Utils.buildJsonResponse("Access Denied",false, resp, HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String path = req.getPathInfo();
        if(path == null || path.equals("/")) {
            Utils.buildJsonResponse("Welcome to RxFlow Admin API",true, resp, HttpServletResponse.SC_OK);
            return;
        }

        String[] parts = path.split("/");

        if(parts.length < 2) {
            Utils.buildJsonResponse("invalid path",false, resp, HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String resource = parts[1];

        switch(resource){

            case "doctors":
                if(parts.length == 2 || parts[2].isEmpty()) {
                    req.getRequestDispatcher("/api/admin/doctors").forward(req, resp);
                }else if(parts.length == 3 && parts[2].equals("change-availability")){
                    req.getRequestDispatcher("/doctors/change-availability").forward(req, resp);
                } else {
                    if(parts.length == 3){
                        req.setAttribute("doctorId", parts[2]);
                    }
                    req.getRequestDispatcher("/api/admin/doctors/*").forward(req, resp);
                }
                break;
            case "users":
                if(parts.length == 2 ){
                    req.getRequestDispatcher("/api/admin/users").forward(req, resp);
                }else {
                    if(parts.length == 3 && !parts[2].isEmpty()){
                        req.setAttribute("userId", parts[2]);
                    }
                    req.getRequestDispatcher("/api/admin/users/*").forward(req, resp);
                }
                break;
            default:
                Utils.buildJsonResponse("no such admin resource",false, resp, HttpServletResponse.SC_BAD_REQUEST);

        }

    }
}
