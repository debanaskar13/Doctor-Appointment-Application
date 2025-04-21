package site.debashisnaskar.rxflow.utils;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import site.debashisnaskar.rxflow.dto.ErrorResponse;
import site.debashisnaskar.rxflow.service.AuthService;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static AuthService authService;
    private static Gson gson;

    public static String readJsonBody(HttpServletRequest request) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        StringBuilder jsonString = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null) {
            jsonString.append(line);
        }
        reader.close();

        return jsonString.toString();
    }

    public static AuthService getAuthServiceInstance() {
        if(authService == null) {
            authService = new AuthService();
        }
        return authService;
    }

    public static Gson getGsonInstance() {
        if(gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    public static String extractPathVariable(String pathInfo, Pattern pattern) {
        Matcher matcher = pattern.matcher(pathInfo);
        String pathVariable = null;
        if(matcher.matches()) {
            pathVariable = matcher.group(1);
        }

        return pathVariable;
    }

    public static void buildJsonResponse(String message, boolean success, HttpServletResponse resp, int status) throws IOException {
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        ErrorResponse errorResponse = ErrorResponse.builder().success(success).message(message).build();
        resp.setStatus(status);
        writer.write(getGsonInstance().toJson(errorResponse));
        writer.flush();
        writer.close();
    }

    public static File convertPartToFile(Part part) throws IOException {
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
