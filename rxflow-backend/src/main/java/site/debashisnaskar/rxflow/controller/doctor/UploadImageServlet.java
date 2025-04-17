package site.debashisnaskar.rxflow.controller.doctor;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import site.debashisnaskar.rxflow.service.DoctorService;
import site.debashisnaskar.rxflow.utils.CloudinaryUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
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
        File file = convertPartToFile(part);

        String doctorId = req.getParameter("doctorId");

        Cloudinary cloudinary = CloudinaryUtil.getInstance();

        try {


            Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());

            String imageUrl = uploadResult.get("secure_url").toString();
            file.delete();

            boolean response = doctorService.uploadImage(imageUrl, Integer.parseInt(doctorId));
            if (!response) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"success\": false, \"message\": \"internal server error\"}");
                return;
            }

            resp.getWriter().write("{\"image_url\":\"" + imageUrl + "\"}");

        }catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"success\":false,\"message\":\"invalid doctor id\"}");
        }catch (Exception e){
            resp.getWriter().write(e.getMessage());
        }

    }

    private File convertPartToFile(Part part) throws IOException {
        File tempFile = File.createTempFile("upload-", part.getSubmittedFileName());
        try (InputStream input = part.getInputStream();
             FileOutputStream output = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        }
        return tempFile;
    }
}
