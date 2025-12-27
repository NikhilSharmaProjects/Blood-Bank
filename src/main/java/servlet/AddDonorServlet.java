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
 * Servlet for adding new donors (Admin only)
 */
public class AddDonorServlet extends HttpServlet {
    
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
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Name is required\"}");
            return;
        }
        if (age == null || age.trim().isEmpty()) {
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Age is required\"}");
            return;
        }
        if (gender == null || gender.trim().isEmpty()) {
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Gender is required\"}");
            return;
        }
        if (bloodGroup == null || bloodGroup.trim().isEmpty()) {
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Blood group is required\"}");
            return;
        }
        if (contact == null || contact.trim().isEmpty()) {
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Contact is required\"}");
            return;
        }
        if (password == null || password.trim().isEmpty()) {
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Password is required\"}");
            return;
        }
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "INSERT INTO donor (name, age, gender, blood_group, contact, password) " +
                          "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            
            // Parse age with error handling
            try {
                int ageValue = Integer.parseInt(age.trim());
                if (ageValue < 18 || ageValue > 65) {
                    response.setContentType("application/json");
                    response.getWriter().write("{\"success\": false, \"message\": \"Age must be between 18 and 65\"}");
                    return;
                }
                stmt.setInt(2, ageValue);
            } catch (NumberFormatException e) {
                response.setContentType("application/json");
                response.getWriter().write("{\"success\": false, \"message\": \"Invalid age format\"}");
                return;
            }
            
            stmt.setString(3, gender);
            stmt.setString(4, bloodGroup);
            stmt.setString(5, contact);
            stmt.setString(6, password);
            
            int result = stmt.executeUpdate();
            
            response.setContentType("application/json");
            if (result > 0) {
                response.getWriter().write("{\"success\": true, \"message\": \"Donor added successfully\"}");
            } else {
                response.getWriter().write("{\"success\": false, \"message\": \"Failed to add donor\"}");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}
