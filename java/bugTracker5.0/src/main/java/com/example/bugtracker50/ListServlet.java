package com.example.bugtracker50;

import java.io.*;
import java.util.List;

import com.google.gson.Gson;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.IssueListRecord;
import model.IssueManager;

@WebServlet("/list-issues")
public class ListServlet extends HttpServlet {
    public void init() {
    }

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