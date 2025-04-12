package site.debashisnaskar.rxflow.utils;

import jakarta.servlet.http.HttpServletRequest;
import site.debashisnaskar.rxflow.service.AuthService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Utils {

    private static AuthService authService;

    public static String readJsonBody(HttpServletRequest request) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        StringBuilder jsonString = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null) {
            jsonString.append(line);
        }

        return jsonString.toString();
    }

    public static AuthService getAuthServiceInstance() {
        if(authService == null) {
            authService = new AuthService();
        }
        return authService;
    }

}
