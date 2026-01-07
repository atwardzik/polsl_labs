package com.example.bugtracker50;

import com.google.gson.Gson;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import model.Issue;
import model.IssueManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet("/issue-details")
public class IssueDetailsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json;charset=UTF-8");

        ServletContext context = request.getServletContext();
        IssueManager manager = (IssueManager) context.getAttribute("IssueManager");

        String id = request.getParameter("id");
        Optional<Issue> issue = manager.findIssueByIdFragment(id);

        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        if (issue.isPresent()) {
            String json = gson.toJson(issue.get().getFullRecord());
            out.write(json);
        } else {
            out.write(gson.toJson("Issue not found"));
        }
    }
}

