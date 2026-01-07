package com.example.bugtracker50;

import com.google.gson.Gson;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.IssueListRecord;
import model.IssueManager;

import java.io.IOException;
import java.util.List;

@WebServlet("/new-issue-servlet")
public class NewIssueServlet extends HttpServlet {
    public void init() {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ServletContext context = request.getServletContext();
        IssueManager manager = (IssueManager) context.getAttribute("IssueManager");

        List<IssueListRecord> issueList = manager.getAllIssuesRecords();

        Gson gson = new Gson();
        String json = gson.toJson(issueList);

        response.getWriter().write(json);
    }

    public void destroy() {
    }
}
