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
 * Servlet for viewing blood stock
 */
public class ViewBloodStockServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM blood_stock ORDER BY blood_group";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            List<Map<String, Object>> stockList = new ArrayList<>();
            
            while (rs.next()) {
                Map<String, Object> stock = new HashMap<>();
                stock.put("bloodGroup", rs.getString("blood_group"));
                stock.put("units", rs.getInt("units"));
                stockList.add(stock);
            }
            
            response.setContentType("application/json");
            response.getWriter().write(new Gson().toJson(stockList));
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
