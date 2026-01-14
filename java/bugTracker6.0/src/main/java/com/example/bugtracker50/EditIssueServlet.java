package com.example.bugtracker50;

import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

/**
 * EditIssueServlet handles POST requests to edit an existing issue.
 * It updates optional fields like title, status, priority, due date, and description
 * for the issue identified by its ID fragment.
 * <p>
 * If the issue ID is missing or invalid, it responds with HTTP 400 (Bad Request) or 404 (Not Found).
 * Any invalid field values also result in HTTP 400.
 *
 * @author Artur Twardzik
 * @version 0.6
 */
@WebServlet("/edit-issue")
public class EditIssueServlet extends HttpServlet {
    public void init() {
    }

    /**
     * Handles POST requests to update an issue's details.
     *
     * @param request  the HttpServletRequest containing parameters for issue update
     * @param response the HttpServletResponse used to send status codes
     * @throws IOException if an I/O error occurs while processing the request
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        ServletContext context = request.getServletContext();
        IssueManager manager = (IssueManager) context.getAttribute("IssueManager");

        String id = request.getParameter("id");
        if (id == null || id.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Issue issue;
        try {
            issue = manager.getIssue(UUID.fromString(id));
        } catch (IssueNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        /* -------- OPTIONAL FIELDS -------- */

        try {
            String title = request.getParameter("title");
            if (title != null) {
                issue.setTitle(title.trim());
            }

            String statusParam = request.getParameter("status");
            if (statusParam != null) {
                issue.setStatus(BugStatus.valueOf(statusParam));
            }

            String priorityParam = request.getParameter("priority");
            if (priorityParam != null) {
                issue.setPriority(Priority.valueOf(priorityParam));
            }

            String dueDateParam = request.getParameter("dueDate");
            if (dueDateParam != null && !dueDateParam.isBlank()) {
                issue.setDueDate(LocalDate.parse(dueDateParam).atStartOfDay());
            }

            String description = request.getParameter("description");
            if (description != null) {
                issue.setDescription(description); //sanitize....?
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void destroy() {
    }
}
