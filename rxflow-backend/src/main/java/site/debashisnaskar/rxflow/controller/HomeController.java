package site.debashisnaskar.rxflow.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.debashisnaskar.rxflow.utils.Utils;

import java.io.IOException;

@WebServlet("")
public class HomeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utils.buildJsonResponse("Welcome to RxFlow Rest API",true,resp,HttpServletResponse.SC_OK);
    }
}
