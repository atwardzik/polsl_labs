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

@WebServlet("/users-list")
public class UsersListServlet extends HttpServlet {
    public void init() {
    }

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

