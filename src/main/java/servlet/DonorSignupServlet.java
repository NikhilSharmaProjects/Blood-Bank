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
        
        // Validate required parameters
        if (name == null || name.trim().isEmpty()) {
            response.sendRedirect("donor-signup.html?error=Name is required");
            return;
        }
        if (age == null || age.trim().isEmpty()) {
            response.sendRedirect("donor-signup.html?error=Age is required");
            return;
        }
        if (gender == null || gender.trim().isEmpty()) {
            response.sendRedirect("donor-signup.html?error=Gender is required");
            return;
        }
        if (bloodGroup == null || bloodGroup.trim().isEmpty()) {
            response.sendRedirect("donor-signup.html?error=Blood group is required");
            return;
        }
        if (contact == null || contact.trim().isEmpty()) {
            response.sendRedirect("donor-signup.html?error=Contact is required");
            return;
        }
        if (password == null || password.trim().isEmpty()) {
            response.sendRedirect("donor-signup.html?error=Password is required");
            return;
        }
        
        int ageValue;
        try {
            ageValue = Integer.parseInt(age.trim());
            if (ageValue < 18 || ageValue > 65) {
                response.sendRedirect("donor-signup.html?error=Age must be between 18 and 65");
                return;
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("donor-signup.html?error=Invalid age format");
            return;
        }
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "INSERT INTO donor (name, age, gender, blood_group, contact, password) " +
                          "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setInt(2, ageValue);
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
