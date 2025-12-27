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
 * Servlet for donor login authentication
 */
public class DonorLoginServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String contact = request.getParameter("contact");
        String password = request.getParameter("password");
        
        // Validate credentials
        if (contact == null || contact.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            response.sendRedirect("donor-login.html?error=Please enter both contact and password");
            return;
        }
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM donor WHERE contact = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, contact);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                // Login successful
                HttpSession session = request.getSession();
                session.setAttribute("userType", "donor");
                session.setAttribute("donorId", rs.getInt("id"));
                session.setAttribute("donorName", rs.getString("name"));
                session.setAttribute("donorContact", contact);
                session.setAttribute("bloodGroup", rs.getString("blood_group"));
                
                response.sendRedirect("donor-dashboard.html");
            } else {
                // Login failed
                response.sendRedirect("donor-login.html?error=Invalid credentials");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("donor-login.html?error=Database error");
        }
    }
}
