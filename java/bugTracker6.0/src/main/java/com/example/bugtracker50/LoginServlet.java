package com.example.bugtracker50;

import com.google.gson.Gson;
import com.password4j.Password;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Issue;
import model.IssueNotFoundException;
import model.User;
import model.UserManager;

import java.io.IOException;
import java.util.UUID;

/**
 * LoginServlet handles POST requests for user authentication.
 * <p>
 * It verifies the provided username and password against the UserManager.
 * If authentication succeeds, it creates a session and returns the user record as JSON.
 * If authentication fails or input is invalid, it responds with the appropriate HTTP status and message.
 * </p>
 * <p>
 *
 * @author Artur Twardzik
 * @version 0.5
 */
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
        User user = userManager.getUser(username);

        if (user == null) {
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


        boolean verified = Password
                .check(password, user.getPasswordHash())
                .addPepper("shared-secret")
                .withArgon2();

        if (verified) {
            HttpSession session = request.getSession(true);
            session.setAttribute("userID", user.getId().toString());

            Gson gson = new Gson();
            String json = gson.toJson(user.getRecord());

            response.getWriter().write(json);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("text/plain");
            response.getWriter().write("Invalid username or password");
        }
    }

    public void destroy() {
    }
}
