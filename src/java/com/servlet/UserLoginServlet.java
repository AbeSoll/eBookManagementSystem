package com.servlet;

import com.dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UserLoginServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("lEmail");
        String password = request.getParameter("lPassword");

        if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email and Password must not be null or empty");
            return;
        }

        email = email.trim();
        password = password.trim();

        // Debugging statements
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);

        UserDAO dao = new UserDAO();
        String loginResult = dao.userLogin(email, password);

        // Debugging statement
        System.out.println("Login Result: " + loginResult);

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            if ("done".equals(loginResult)) {
                HttpSession session = request.getSession();
                Object userDetail = dao.userDetail(email);
                if (userDetail == null) {
                    out.println("User details not found");
                } else {
                    session.setAttribute("userL", userDetail);
                    out.println("Login Successful");
                }
            } else if ("invalid".equals(loginResult)) {
                out.println("Invalid Credentials");
            } else {
                out.println("Login Failed");
            }
        } catch (Exception e) {
            // Log error message
            System.err.println("Error during login process: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An internal error occurred");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "User Login Servlet";
    }
}
