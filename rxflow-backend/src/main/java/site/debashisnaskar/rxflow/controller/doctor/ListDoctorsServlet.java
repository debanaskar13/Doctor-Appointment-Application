package site.debashisnaskar.rxflow.controller.doctor;


import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.debashisnaskar.rxflow.dto.DoctorDto;
import site.debashisnaskar.rxflow.service.DoctorService;
import site.debashisnaskar.rxflow.utils.Utils;

import java.io.IOException;
import java.util.List;

@WebServlet("/doctors/list")
public class ListDoctorsServlet extends HttpServlet {
    private static final DoctorService doctorService = new DoctorService();
    private static  final Gson gson = Utils.getGsonInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try{
            List<DoctorDto> doctorList = doctorService.getDoctorList();
            String responseBody = gson.toJson(doctorList);

            resp.setContentType("application/json");
            resp.getWriter().write("{\"success\":true,\"doctors\":"+responseBody+"}");

        }catch (Exception e){
            Utils.buildJsonResponse(e.getMessage(),false,resp,HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
