package com.example.bugtracker50;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * LogoutServlet handles user logout requests.
 * <p>
 * It invalidates the current session (if one exists) and redirects the user to "index.jsp".
 * Both GET and POST requests are supported.
 * </p>
 * <p>
 * @author Artur Twardzik
 * @version 0.5
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    public void init() {
    }

    /**
     * Processes logout requests by invalidating the session and redirecting to index.jsp.
     *
     * @param request  the HttpServletRequest for the current session
     * @param response the HttpServletResponse used to set status and redirect
     * @throws IOException if an I/O error occurs during redirection
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false); // don't create new

        if (session != null) {
            session.invalidate();
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect("index.jsp");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    public void destroy() {
    }
}
