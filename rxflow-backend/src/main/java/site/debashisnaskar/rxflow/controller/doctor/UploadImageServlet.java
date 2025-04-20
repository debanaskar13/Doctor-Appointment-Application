package site.debashisnaskar.rxflow.controller.doctor;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import site.debashisnaskar.rxflow.service.DoctorService;
import site.debashisnaskar.rxflow.utils.Utils;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/doctors/upload")
@MultipartConfig
public class UploadImageServlet extends HttpServlet {

    private static final DoctorService doctorService = new DoctorService();
    private static final Logger logger = Logger.getLogger(UploadImageServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        Part part = req.getPart("image");

        String doctorId = req.getParameter("doctorId");

        try {
            boolean response = doctorService.uploadImage(part, Integer.parseInt(doctorId));
            if (!response) {
                Utils.buildJsonResponse("internal server error",false,resp,HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                logger.severe("Internal server error : ");
                return;
            }

            Utils.buildJsonResponse("image uploaded successfully",true,resp,HttpServletResponse.SC_OK);

        }catch (NumberFormatException e) {
            Utils.buildJsonResponse("invalid doctor id",false,resp,HttpServletResponse.SC_BAD_REQUEST);
            logger.severe("invalid doctor id , error : " + e.getMessage());
        }catch (Exception e){
            Utils.buildJsonResponse("something went wrong",false,resp,HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.severe("Internal server error : " + e.getMessage());
        }

    }


}
