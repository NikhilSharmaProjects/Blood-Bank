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
 * Servlet for searching donors by blood group
 */
public class SearchDonorServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String bloodGroup = request.getParameter("bloodGroup");
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM donor WHERE blood_group = ? ORDER BY registration_date DESC";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, bloodGroup);
            
            ResultSet rs = stmt.executeQuery();
            List<Map<String, Object>> donors = new ArrayList<>();
            
            while (rs.next()) {
                Map<String, Object> donor = new HashMap<>();
                donor.put("id", rs.getInt("id"));
                donor.put("name", rs.getString("name"));
                donor.put("age", rs.getInt("age"));
                donor.put("gender", rs.getString("gender"));
                donor.put("bloodGroup", rs.getString("blood_group"));
                donor.put("contact", rs.getString("contact"));
                donor.put("registrationDate", rs.getTimestamp("registration_date").toString());
                donors.add(donor);
            }
            
            response.setContentType("application/json");
            response.getWriter().write(new Gson().toJson(donors));
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
