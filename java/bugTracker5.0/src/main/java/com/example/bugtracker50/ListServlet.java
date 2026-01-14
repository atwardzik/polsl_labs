package com.example.bugtracker50;

import java.io.*;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

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
 *
 * @author Artur Twardzik
 * @version 0.5
 */
@WebServlet("/list-issues")
public class ListServlet extends HttpServlet {
    public void init() {
    }

    /**
     * Resolves the locale for the request, using the "lang" cookie if present.
     *
     * @param request the HttpServletRequest to resolve the locale from
     * @return the resolved Locale
     */
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

        Locale locale = resolve(request);
        ResourceBundle b = ResourceBundle.getBundle("i18n.messages", locale);

        Map<String, String> lcl = Map.ofEntries(
                Map.entry("author", b.getString("issue.author")),
                Map.entry("createdOn", b.getString("issue.createdOn")),
                Map.entry("dueOn", b.getString("issue.dueOn")),
                Map.entry("IN_PROGRESS", b.getString("issue.IN_PROGRESS")),
                Map.entry("OPEN", b.getString("issue.OPEN")),
                Map.entry("CLOSED", b.getString("issue.CLOSED")),
                Map.entry("REOPENED", b.getString("issue.REOPENED"))
        );

        Gson gson = new Gson();
        JsonResponse res = new JsonResponse<>(issueList, lcl);
        String json = gson.toJson(res);

        response.getWriter().write(json);
    }

    public void destroy() {
    }
}