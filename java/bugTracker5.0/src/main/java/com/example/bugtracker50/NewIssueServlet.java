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
        IssueManager manager = (IssueManager) context.getAttribute("IssueManager");

        try {
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String statusParam = request.getParameter("status");
            String priorityParam = request.getParameter("priority");
            String dueDateParam = request.getParameter("dueDate");
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

            // Create a dummy user (replace with logged-in user if available)
            User creator = new User("", "", "");

            Issue issue = new Issue(
                    title,
                    description,
                    creator,
                    dueDateTime,
                    null,
                    status,
                    priority,
                    null
            );

            manager.addIssue(issue);
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create issue: " + e.getMessage());
        }
    }

    public void destroy() {
    }
}
