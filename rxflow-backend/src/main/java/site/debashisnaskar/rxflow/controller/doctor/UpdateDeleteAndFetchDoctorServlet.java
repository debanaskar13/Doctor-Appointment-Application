package site.debashisnaskar.rxflow.controller.doctor;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/doctors/*")
public class UpdateDeleteAndFetchDoctorServlet extends HttpServlet {

    private static final Pattern pattern = Pattern.compile("^/([0-9]+)/?$");

//    Fetch Specific Doctor by ID
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        String pathInfo = req.getPathInfo();
        String doctorId = extractDoctorId(pathInfo);

        if(doctorId != null) {
            resp.getWriter().write("\"doctorId\": \"" + doctorId + "\" ");
        }

    }

//    Update Specific Doctor by ID
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String pathInfo = req.getPathInfo();
        String doctorId = extractDoctorId(pathInfo);

        if(doctorId != null) {
            resp.getWriter().write("\"doctorId\": \"" + doctorId + "\" ");
        }

    }

//    Delete Specific Doctor by ID
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String pathInfo = req.getPathInfo();
        String doctorId = extractDoctorId(pathInfo);

        if(doctorId != null) {
            resp.getWriter().write("\"doctorId\": \"" + doctorId + "\" ");
        }
    }


    private String extractDoctorId(String pathInfo) {
        Matcher matcher = pattern.matcher(pathInfo);
        String doctorId = null;
        if(matcher.matches()) {
             doctorId = matcher.group(1);
        }

        return doctorId;
    }
}
