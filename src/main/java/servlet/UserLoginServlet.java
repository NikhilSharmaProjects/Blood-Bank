package servlet;

import util.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Servlet for user (blood receiver) login authentication
 */
public class UserLoginServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                // Login successful
                HttpSession session = request.getSession();
                session.setAttribute("userType", "user");
                session.setAttribute("userId", rs.getInt("id"));
                session.setAttribute("username", username);
                session.setAttribute("userName", rs.getString("name"));
                session.setAttribute("userContact", rs.getString("contact"));
                
                response.sendRedirect("user-dashboard.html");
            } else {
                // Login failed
                response.sendRedirect("user-login.html?error=Invalid credentials");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("user-login.html?error=Database error");
        }
    }
}
