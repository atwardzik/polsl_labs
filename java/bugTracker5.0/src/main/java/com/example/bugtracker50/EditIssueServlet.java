package com.example.bugtracker50;

import com.google.gson.Gson;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.BugStatus;
import model.Issue;
import model.IssueListRecord;
import model.IssueManager;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/edit-issue-servlet")
public class EditIssueServlet extends HttpServlet {
    public void init() {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        ServletContext context = request.getServletContext();
        IssueManager manager = (IssueManager) context.getAttribute("IssueManager");

        String id = request.getParameter("id");
        String statusParam = request.getParameter("status");
        BugStatus status;
        try {
            status = BugStatus.valueOf(statusParam);
        } catch (IllegalArgumentException _) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (id == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Optional<Issue> issue = manager.findIssueByIdFragment(id);
        if (issue.isPresent()) {
            issue.get().setStatus(status);

            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public void destroy() {
    }
}
