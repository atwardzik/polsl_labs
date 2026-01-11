package com.example.bugtracker50;

import com.google.gson.Gson;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.UserManager;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * UsersListServlet handles GET requests to retrieve the list of all users in JSON format.
 * <p>
 * Response content type is set to "application/json" with UTF-8 encoding.
 * In case of errors during processing, it responds with HTTP 500 Internal Server Error.
 * </p>
 *
 * @author Artur Twardzik
 * @version 0.5
 */
@WebServlet("/users-list")
public class UsersListServlet extends HttpServlet {
    public void init() {
    }

    /**
     * Handles GET requests to return all users as JSON.
     *
     * @param request  the HttpServletRequest
     * @param response the HttpServletResponse used to write JSON output or error
     * @throws IOException if an I/O error occurs while writing the response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ServletContext context = request.getServletContext();
        UserManager userManager = (UserManager) context.getAttribute("UserManager");

        try {
            Gson gson = new Gson();
            String json = gson.toJson(userManager.getUserList());
            response.getWriter().write(json);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public void destroy() {
    }
}

