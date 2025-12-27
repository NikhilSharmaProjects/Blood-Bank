package servlet;

import com.google.gson.Gson;
import util.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet for dashboard statistics
 */
public class DashboardStatsServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try (Connection conn = DBConnection.getConnection()) {
            Map<String, Object> stats = new HashMap<>();
            
            // Get total donors
            String donorQuery = "SELECT COUNT(*) as total FROM donor";
            PreparedStatement donorStmt = conn.prepareStatement(donorQuery);
            ResultSet donorRs = donorStmt.executeQuery();
            if (donorRs.next()) {
                stats.put("totalDonors", donorRs.getInt("total"));
            }
            
            // Get total blood units
            String stockQuery = "SELECT SUM(units) as total FROM blood_stock";
            PreparedStatement stockStmt = conn.prepareStatement(stockQuery);
            ResultSet stockRs = stockStmt.executeQuery();
            if (stockRs.next()) {
                stats.put("totalBloodUnits", stockRs.getInt("total"));
            }
            
            // Get total blood issues
            String issueQuery = "SELECT COUNT(*) as total FROM blood_issue";
            PreparedStatement issueStmt = conn.prepareStatement(issueQuery);
            ResultSet issueRs = issueStmt.executeQuery();
            if (issueRs.next()) {
                stats.put("totalIssues", issueRs.getInt("total"));
            }
            
            // Get total users
            String userQuery = "SELECT COUNT(*) as total FROM users";
            PreparedStatement userStmt = conn.prepareStatement(userQuery);
            ResultSet userRs = userStmt.executeQuery();
            if (userRs.next()) {
                stats.put("totalUsers", userRs.getInt("total"));
            }
            
            response.setContentType("application/json");
            response.getWriter().write(new Gson().toJson(stats));
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
