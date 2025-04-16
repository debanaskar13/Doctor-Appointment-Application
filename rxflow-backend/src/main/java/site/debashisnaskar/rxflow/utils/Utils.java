package site.debashisnaskar.rxflow.utils;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import site.debashisnaskar.rxflow.service.AuthService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

}
