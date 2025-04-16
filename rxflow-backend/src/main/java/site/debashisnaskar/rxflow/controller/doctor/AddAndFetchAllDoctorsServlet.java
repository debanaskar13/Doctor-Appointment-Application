package site.debashisnaskar.rxflow.controller.doctor;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.debashisnaskar.rxflow.model.Doctor;
import site.debashisnaskar.rxflow.utils.Utils;

import java.io.IOException;

@WebServlet("/doctors")
public class AddAndFetchAllDoctorsServlet extends HttpServlet {

//    Fetch All Doctors
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

//    Add Doctor
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String requestBody = Utils.readJsonBody(req);

        Gson gson = new Gson();
        Doctor doctor = gson.fromJson(req.getReader(), Doctor.class);

    }
}
