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

@WebServlet("/edit-issue")
public class EditIssueServlet extends HttpServlet {
    public void init() {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        ServletContext context = request.getServletContext();
        IssueManager manager = (IssueManager) context.getAttribute("IssueManager");

        String id = request.getParameter("id");
        if (id == null || id.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Optional<Issue> optIssue = manager.findIssueByIdFragment(id);
        if (optIssue.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Issue issue = optIssue.get();

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
