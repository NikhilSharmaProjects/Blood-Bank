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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet for viewing blood issue history
 */
public class ViewBloodIssueServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT bi.*, u.name as user_name FROM blood_issue bi " +
                          "LEFT JOIN users u ON bi.user_id = u.id " +
                          "ORDER BY bi.issue_date DESC";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            List<Map<String, Object>> issueList = new ArrayList<>();
            
            while (rs.next()) {
                Map<String, Object> issue = new HashMap<>();
                issue.put("id", rs.getInt("id"));
                issue.put("patientName", rs.getString("patient_name"));
                issue.put("bloodGroup", rs.getString("blood_group"));
                issue.put("units", rs.getInt("units"));
                issue.put("receiverAddress", rs.getString("receiver_address"));
                issue.put("issueDate", rs.getTimestamp("issue_date").toString());
                issue.put("userName", rs.getString("user_name"));
                issueList.add(issue);
            }
            
            response.setContentType("application/json");
            response.getWriter().write(new Gson().toJson(issueList));
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
