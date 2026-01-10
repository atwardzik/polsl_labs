package com.example.bugtracker50;

import com.google.gson.Gson;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@WebServlet("/new-issue-servlet")
public class NewIssueServlet extends HttpServlet {
    public void init() {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletContext context = request.getServletContext();
        IssueManager issueManager = (IssueManager) context.getAttribute("IssueManager");
        UserManager userManager = (UserManager) context.getAttribute("UserManager");

        response.setCharacterEncoding("UTF-8");

        try {
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String statusParam = request.getParameter("status");
            String priorityParam = request.getParameter("priority");
            String dueDateParam = request.getParameter("dueDate");
            String assignee = request.getParameter("assignee");
            String tagsParam = request.getParameter("tags");

            if (title == null || title.isBlank()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("text/plain");
                response.getWriter().write("Title should not be empty!");
                response.getWriter().flush();
                return;
            }

            if (description == null || description.isBlank()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("text/plain");
                response.getWriter().write("Description should not be empty!");
                response.getWriter().flush();
                return;
            }

            BugStatus status;
            try {
                status = BugStatus.valueOf(statusParam);
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid status value!");
                return;
            }

            Priority priority;
            try {
                priority = Priority.valueOf(priorityParam);
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid priority value!");
                return;
            }

            LocalDateTime dueDateTime = null;
            if (dueDateParam != null && !dueDateParam.isBlank()) {
                try {
                    dueDateTime = LocalDate.parse(dueDateParam).atStartOfDay();
                } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid due date format!");
                    return;
                }
            }

            User assignedUser = null;
            if (assignee != null && !assignee.isBlank()) {
                if (userManager.usernameExists(assignee)) {
                    assignedUser = userManager.getUser(assignee);
                }
            }

            // Create a dummy user (replace with logged-in user if available)
            User creator = new User("", "", "", "");

            Issue issue = new Issue(
                    title,
                    description,
                    creator,
                    dueDateTime,
                    assignedUser,
                    status,
                    priority,
                    null
            );

            issueManager.addIssue(issue);
            response.setStatus(HttpServletResponse.SC_OK);

            response.setContentType("application/json");
            Gson gson = new Gson();
            String json = gson.toJson(issue.getRecord());
            response.getWriter().write(json);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create issue: " + e.getMessage());
        }
    }

    public void destroy() {
    }
}
