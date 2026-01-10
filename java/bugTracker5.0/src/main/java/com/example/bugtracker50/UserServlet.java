package com.example.bugtracker50;

import com.google.gson.Gson;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.UserManager;

import java.io.IOException;

@WebServlet("/user-details")
public class UserServlet extends HttpServlet {
    public void init() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json;charset=UTF-8");

        ServletContext context = request.getServletContext();
        UserManager userManager = (UserManager) context.getAttribute("UserManager");

        String username = request.getParameter("username");
        if (username == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("text/plain");
            response.getWriter().write("Username should not be empty!");
            response.getWriter().flush();
            return;
        }

        if (userManager.usernameExists(username)) {
            Gson gson = new Gson();
            String json = gson.toJson(userManager.getUser(username).getFullRecord());
            response.getWriter().write(json);
        }
    }

    public void destroy() {
    }
}

