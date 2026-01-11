package com.example.bugtracker50;

import java.io.*;
import java.util.List;

import com.google.gson.Gson;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.IssueListRecord;
import model.IssueManager;

/**
 * ListServlet handles GET requests to retrieve a list of all issues in JSON format.
 * Optionally, issues can be filtered by assignee using the "username" request parameter.
 * <p>
 * Response content type is set to "application/json" with UTF-8 encoding.
 * <p>
 * @author Artur Twardzik
 * @version 0.5
 */
@WebServlet("/list-issues")
public class ListServlet extends HttpServlet {
    public void init() {
    }

    /**
     * Handles GET requests to return a JSON list of issues.
     *
     * @param request  the HttpServletRequest containing optional "username" filter
     * @param response the HttpServletResponse used to write JSON output
     * @throws IOException if an I/O error occurs while writing the response
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ServletContext context = request.getServletContext();
        IssueManager manager = (IssueManager) context.getAttribute("IssueManager");

        List<IssueListRecord> issueList = manager.getAllIssuesRecords();
        String username = request.getParameter("username");
        if (username != null) {
            issueList = issueList.stream().filter(issue ->
                    issue.assignee().equals(username)
            ).toList();
        }


        Gson gson = new Gson();
        String json = gson.toJson(issueList);

        response.getWriter().write(json);
    }

    public void destroy() {
    }
}