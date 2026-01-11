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
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

@WebServlet("/issue-details")
public class IssueDetailsServlet extends HttpServlet {
    public static Locale resolve(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return request.getLocale();
        }

        for (Cookie c : request.getCookies()) {
            if ("lang".equals(c.getName())) {
                return Locale.forLanguageTag(c.getValue());
            }
        }

        return request.getLocale();
    }

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
            Locale locale = resolve(request);
            ResourceBundle b = ResourceBundle.getBundle("i18n.messages", locale);

            Map<String, String> lcl = Map.ofEntries(
                    Map.entry("id", b.getString("issue.id")),
                    Map.entry("author", b.getString("issue.author")),
                    Map.entry("status", b.getString("issue.status")),
                    Map.entry("priority", b.getString("issue.priority")),
                    Map.entry("createdOn", b.getString("issue.createdOn")),
                    Map.entry("dueOn", b.getString("issue.dueOn")),
                    Map.entry("IN_PROGRESS", b.getString("issue.IN_PROGRESS")),
                    Map.entry("OPEN", b.getString("issue.OPEN")),
                    Map.entry("CLOSED", b.getString("issue.CLOSED")),
                    Map.entry("REOPENED", b.getString("issue.REOPENED")),
                    Map.entry("LOW", b.getString("issue.LOW")),
                    Map.entry("MEDIUM", b.getString("issue.MEDIUM")),
                    Map.entry("HIGH", b.getString("issue.HIGH")),
                    Map.entry("CRITICAL", b.getString("issue.CRITICAL"))
            );

            JsonResponse res = new JsonResponse<>(issue.get().getFullRecord(), lcl);
            String json = gson.toJson(res);

            out.write(json);
        } else {
            out.write(gson.toJson("Issue not found"));
        }
    }
}

