package com.example.bugtracker50;

import com.password4j.Hash;
import com.password4j.Password;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import model.UserManager;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    public void init() {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ServletContext context = request.getServletContext();
        UserManager userManager = (UserManager) context.getAttribute("UserManager");
        String username = request.getParameter("username");
        if (username == null || !userManager.usernameExists(username)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("text/plain");
            response.getWriter().write("No such user exists");
            response.getWriter().flush();
            return;
        }


        String password = request.getParameter("password");
        if (password == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("text/plain");
            response.getWriter().write("Password is empty");
            response.getWriter().flush();
            return;
        }

        Hash hash = Password.hash(password)
                .addPepper("shared-secret")
                .addRandomSalt(32)
                .withArgon2();
        String passwordHash = hash.getResult();

        User user = userManager.getUser(username);
        if (user.getPasswordHash().equals(passwordHash)) {
            HttpSession session = request.getSession(true);
            session.setAttribute("userID", user.getId().toString());

            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    public void destroy() {
    }
}
