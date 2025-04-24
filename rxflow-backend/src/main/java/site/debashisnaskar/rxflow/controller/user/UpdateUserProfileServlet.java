package site.debashisnaskar.rxflow.controller.user;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import site.debashisnaskar.rxflow.dto.UpdateProfileRequest;
import site.debashisnaskar.rxflow.model.User;
import site.debashisnaskar.rxflow.service.UserService;
import site.debashisnaskar.rxflow.utils.Utils;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/user/profile/update")
@MultipartConfig
public class UpdateUserProfileServlet extends HttpServlet {
    private static final Gson gson = Utils.getGsonInstance();
    private static final UserService userService = new UserService();
    private static final Logger logger= Logger.getLogger(UpdateUserProfileServlet.class.getName());

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        Part imageFilePart = req.getPart("image");
        String requestBodyJson = req.getParameter("profileData");
        User loggedInUser = (User) req.getAttribute("user");

        UpdateProfileRequest updateProfileRequest = gson.fromJson(requestBodyJson, UpdateProfileRequest.class);

        if(loggedInUser != null) {
            try {
                loggedInUser.setName(updateProfileRequest.getName() != null ? updateProfileRequest.getName() : loggedInUser.getName());
                loggedInUser.setEmail(updateProfileRequest.getEmail() != null ? updateProfileRequest.getEmail() : loggedInUser.getEmail());
                loggedInUser.setPhone(updateProfileRequest.getPhone() != null ? updateProfileRequest.getPhone() : loggedInUser.getPhone());
                loggedInUser.setAddress(updateProfileRequest.getAddress() !=null ? updateProfileRequest.getAddress() :  loggedInUser.getAddress());
                loggedInUser.setDob(updateProfileRequest.getDob() != null ? updateProfileRequest.getDob() : loggedInUser.getDob());
                loggedInUser.setGender(updateProfileRequest.getGender() != null ? updateProfileRequest.getGender() : loggedInUser.getGender());

                boolean isUpdated = userService.updateUserProfile(loggedInUser,imageFilePart);

                if(isUpdated) {
                    Utils.buildJsonResponse("Profile updated successfully", true, resp, HttpServletResponse.SC_OK);
                    logger.info("Profile updated successfully for user " + loggedInUser.getUsername());
                }else{
                    Utils.buildJsonResponse("Profile update failed", false, resp, HttpServletResponse.SC_BAD_REQUEST);
                    logger.info("Profile update failed for user " + loggedInUser.getUsername());
                }
            }catch (Exception e) {
                Utils.buildJsonResponse("Profile update failed error " + e.getMessage(), false, resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                logger.info("Profile update failed for user " + loggedInUser.getUsername() + " error : " + e.getMessage());
            }
        }
    }
}
