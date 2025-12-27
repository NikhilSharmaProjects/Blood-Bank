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
