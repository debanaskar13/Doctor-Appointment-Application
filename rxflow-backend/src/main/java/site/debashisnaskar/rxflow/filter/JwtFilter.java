package site.debashisnaskar.rxflow.filter;


import com.google.gson.Gson;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.debashisnaskar.rxflow.model.Address;
import site.debashisnaskar.rxflow.model.User;
import site.debashisnaskar.rxflow.utils.DB;
import site.debashisnaskar.rxflow.utils.JwtUtil;
import site.debashisnaskar.rxflow.utils.Utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

@WebFilter("/*")
public class JwtFilter implements Filter {

    private static final Logger logger = Logger.getLogger(JwtFilter.class.getName());
    private static final Gson gson = Utils.getGsonInstance();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getRequestURI();

        if(path.contains("/doctors/list") || path.contains("/login") || path.contains("/logout") || path.contains("/register")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if(path.contains("/doctors") && request.getMethod().equals("GET")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        try{
            if(authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String username = JwtUtil.extractUsername(token);

                if(username != null) {

                    User user = null;
                    try {
                        Connection conn = DB.getConnection();

                        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
                        stmt.setString(1, username);
                        ResultSet rs = stmt.executeQuery();

                        if(rs.next()) {
                            user = User.builder()
                                    .id(rs.getInt("id"))
                                    .username(rs.getString("username"))
                                    .role(rs.getString("role"))
                                    .phone(rs.getString("phone"))
                                    .email(rs.getString("email"))
                                    .name(rs.getString("name"))
                                    .image(rs.getString("image"))
                                    .build();
                        }

                        if(user != null && JwtUtil.validateToken(token,user)){
                            request.setAttribute("user", user);
                            filterChain.doFilter(servletRequest, servletResponse);
                            return;
                        }

                    } catch (SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }else{
                Utils.buildJsonResponse("Authorization header is missing or not started with Bearer",false,response,HttpServletResponse.SC_UNAUTHORIZED);
                logger.severe("Authorization header is missing or not started with Bearer");
            }
        }catch(JwtException e){
            Utils.buildJsonResponse("invalid jwt token",false,response,HttpServletResponse.SC_UNAUTHORIZED);
            logger.severe(e.getMessage());
        }catch(Exception e){
            Utils.buildJsonResponse("something went wrong",false,response,HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.severe(e.getMessage());
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    }
}
