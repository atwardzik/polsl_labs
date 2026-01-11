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

/**
 * IssueDetailsServlet handles GET requests to retrieve detailed information
 * about a specific issue in JSON format.
 * <p>
 * The servlet resolves the user's locale using the "lang" cookie if available
 * and localizes field names and statuses accordingly. If the issue is not found,
 * it returns a JSON message indicating that.
 * </p>
 *
 * @author Artur Twardzik
 * @version 0.5
 */
@WebServlet("/issue-details")
public class IssueDetailsServlet extends HttpServlet {

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
     * Handles GET requests to retrieve a specific issue's details as JSON.
     * Localizes field names and statuses according to the user's locale.
     *
     * @param request  the HttpServletRequest containing the issue ID parameter
     * @param response the HttpServletResponse used to write JSON output
     * @throws IOException if an I/O error occurs while writing the response
     */
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

