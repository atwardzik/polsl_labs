package com.example.bugtracker50;

import com.google.gson.Gson;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import model.UserManager;

import java.io.IOException;

@WebServlet("/user-details")
public class UserServlet extends HttpServlet {
    public void init() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        ServletContext context = request.getServletContext();
        UserManager userManager = (UserManager) context.getAttribute("UserManager");

        HttpSession session = request.getSession(false);
        response.setContentType("application/json");

        String userID = (String) session.getAttribute("userID");
        if (session != null && userID != null) {
            Gson gson = new Gson();
            String json = gson.toJson(userManager.getUserById(userID).getFullRecord());
            response.getWriter().write(json);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{}");
        }
    }

    public void destroy() {
    }
}

