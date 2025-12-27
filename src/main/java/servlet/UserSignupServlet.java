package servlet;

import util.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Servlet for user (blood receiver) registration
 */
public class UserSignupServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String contact = request.getParameter("contact");
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "INSERT INTO users (username, password, name, contact) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, name);
            stmt.setString(4, contact);
            
            int result = stmt.executeUpdate();
            
            if (result > 0) {
                // Signup successful
                response.sendRedirect("user-login.html?success=Registration successful");
            } else {
                response.sendRedirect("user-signup.html?error=Registration failed");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("Duplicate entry")) {
                response.sendRedirect("user-signup.html?error=Username already exists");
            } else {
                response.sendRedirect("user-signup.html?error=Database error");
            }
        }
    }
}
