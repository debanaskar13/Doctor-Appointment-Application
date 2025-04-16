package site.debashisnaskar.rxflow.filter;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.debashisnaskar.rxflow.model.User;
import site.debashisnaskar.rxflow.utils.DB;
import site.debashisnaskar.rxflow.utils.JwtUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebFilter("/*")
public class JwtFilter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getRequestURI();

        if(path.contains("/login") || path.contains("/logout") || path.contains("/register")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String authHeader = request.getHeader("Authorization");

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
                                .phone(rs.getString("phone"))
                                .email(rs.getString("email"))
                                .name(rs.getString("name"))
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
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("\"error\":\"Unauthorized\"");
    }
}
