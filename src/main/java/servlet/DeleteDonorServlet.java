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
 * Servlet for deleting donor records
 */
public class DeleteDonorServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String donorId = request.getParameter("donorId");
        
        // Validate required parameter
        if (donorId == null || donorId.trim().isEmpty()) {
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Donor ID is required\"}");
            return;
        }
        
        int donorIdValue;
        try {
            donorIdValue = Integer.parseInt(donorId.trim());
        } catch (NumberFormatException e) {
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid donor ID format\"}");
            return;
        }
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "DELETE FROM donor WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, donorIdValue);
            
            int result = stmt.executeUpdate();
            
            response.setContentType("application/json");
            if (result > 0) {
                response.getWriter().write("{\"success\": true, \"message\": \"Donor deleted successfully\"}");
            } else {
                response.getWriter().write("{\"success\": false, \"message\": \"Donor not found\"}");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}
