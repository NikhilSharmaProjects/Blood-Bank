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
 * Servlet for donor registration
 */
public class DonorSignupServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String name = request.getParameter("name");
        String age = request.getParameter("age");
        String gender = request.getParameter("gender");
        String bloodGroup = request.getParameter("bloodGroup");
        String contact = request.getParameter("contact");
        String password = request.getParameter("password");
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "INSERT INTO donor (name, age, gender, blood_group, contact, password) " +
                          "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setInt(2, Integer.parseInt(age));
            stmt.setString(3, gender);
            stmt.setString(4, bloodGroup);
            stmt.setString(5, contact);
            stmt.setString(6, password);
            
            int result = stmt.executeUpdate();
            
            if (result > 0) {
                // Signup successful
                response.sendRedirect("donor-login.html?success=Registration successful");
            } else {
                response.sendRedirect("donor-signup.html?error=Registration failed");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("Duplicate entry")) {
                response.sendRedirect("donor-signup.html?error=Contact number already registered");
            } else {
                response.sendRedirect("donor-signup.html?error=Database error");
            }
        }
    }
}
