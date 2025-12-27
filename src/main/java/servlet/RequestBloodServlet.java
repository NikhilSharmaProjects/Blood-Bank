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
 * Servlet for requesting/issuing blood
 */
public class RequestBloodServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Please login first\"}");
            return;
        }
        
        String patientName = request.getParameter("patientName");
        String bloodGroup = request.getParameter("bloodGroup");
        String units = request.getParameter("units");
        String receiverAddress = request.getParameter("receiverAddress");
        int userId = (Integer) session.getAttribute("userId");
        
        // Validate required parameters
        if (patientName == null || patientName.trim().isEmpty()) {
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Patient name is required\"}");
            return;
        }
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
        if (receiverAddress == null || receiverAddress.trim().isEmpty()) {
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Receiver address is required\"}");
            return;
        }
        
        int requestedUnits;
        try {
            requestedUnits = Integer.parseInt(units.trim());
            if (requestedUnits <= 0) {
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
            // Check stock availability
            String checkQuery = "SELECT units FROM blood_stock WHERE blood_group = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, bloodGroup);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                int availableUnits = rs.getInt("units");
                
                if (availableUnits >= requestedUnits) {
                    // Stock is available, issue blood
                    conn.setAutoCommit(false);
                    
                    try {
                        // Insert into blood_issue table
                        String issueQuery = "INSERT INTO blood_issue (patient_name, blood_group, units, receiver_address, user_id) " +
                                          "VALUES (?, ?, ?, ?, ?)";
                        PreparedStatement issueStmt = conn.prepareStatement(issueQuery);
                        issueStmt.setString(1, patientName);
                        issueStmt.setString(2, bloodGroup);
                        issueStmt.setInt(3, requestedUnits);
                        issueStmt.setString(4, receiverAddress);
                        issueStmt.setInt(5, userId);
                        issueStmt.executeUpdate();
                        
                        // Update blood stock
                        String updateQuery = "UPDATE blood_stock SET units = units - ? WHERE blood_group = ?";
                        PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                        updateStmt.setInt(1, requestedUnits);
                        updateStmt.setString(2, bloodGroup);
                        updateStmt.executeUpdate();
                        
                        conn.commit();
                        
                        response.setContentType("application/json");
                        response.getWriter().write("{\"success\": true, \"message\": \"Blood issued successfully\"}");
                        
                    } catch (Exception e) {
                        conn.rollback();
                        throw e;
                    } finally {
                        conn.setAutoCommit(true);
                    }
                    
                } else {
                    // Not enough stock
                    response.setContentType("application/json");
                    response.getWriter().write("{\"success\": false, \"message\": \"Not enough stock. Available: " + 
                                              availableUnits + " units\"}");
                }
            } else {
                response.setContentType("application/json");
                response.getWriter().write("{\"success\": false, \"message\": \"Blood group not found\"}");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}
