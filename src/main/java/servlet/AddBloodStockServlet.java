package servlet;

import util.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Servlet for adding blood stock
 */
public class AddBloodStockServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String bloodGroup = request.getParameter("bloodGroup");
        String units = request.getParameter("units");
        
        // Validate required parameters
        if (bloodGroup == null || bloodGroup.trim().isEmpty()) {
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Blood group is required\"}");
            return;
        }
        if (units == null || units.trim().isEmpty()) {
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Units is required\"}");
            return;
        }
        
        int unitsValue;
        try {
            unitsValue = Integer.parseInt(units.trim());
            if (unitsValue <= 0) {
                response.setContentType("application/json");
                response.getWriter().write("{\"success\": false, \"message\": \"Units must be greater than 0\"}");
                return;
            }
        } catch (NumberFormatException e) {
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid units format\"}");
            return;
        }
        
        try (Connection conn = DBConnection.getConnection()) {
            // Check if blood group exists
            String checkQuery = "SELECT units FROM blood_stock WHERE blood_group = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, bloodGroup);
            ResultSet rs = checkStmt.executeQuery();
            
            String query;
            PreparedStatement stmt;
            
            if (rs.next()) {
                // Update existing stock
                int currentUnits = rs.getInt("units");
                int newUnits = currentUnits + unitsValue;
                query = "UPDATE blood_stock SET units = ? WHERE blood_group = ?";
                stmt = conn.prepareStatement(query);
                stmt.setInt(1, newUnits);
                stmt.setString(2, bloodGroup);
            } else {
                // Insert new stock
                query = "INSERT INTO blood_stock (blood_group, units) VALUES (?, ?)";
                stmt = conn.prepareStatement(query);
                stmt.setString(1, bloodGroup);
                stmt.setInt(2, unitsValue);
            }
            
            int result = stmt.executeUpdate();
            
            response.setContentType("application/json");
            if (result > 0) {
                response.getWriter().write("{\"success\": true, \"message\": \"Blood stock added successfully\"}");
            } else {
                response.getWriter().write("{\"success\": false, \"message\": \"Failed to add blood stock\"}");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}
