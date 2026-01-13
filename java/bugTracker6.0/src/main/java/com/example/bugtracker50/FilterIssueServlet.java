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

/**
 * FilterIssueServlet handles POST requests to retrieve all issues as JSON.
 * It fetches issue records from the IssueManager and returns them in JSON format.
 * <p>
 * Sets response content type to "application/json" and character encoding to UTF-8.
 *
 * @author Artur Twardzik
 * @version 0.5
 */
@WebServlet("/filter-issue")
public class FilterIssueServlet extends HttpServlet {
    public void init() {
    }

    /**
     * Handles POST requests to return all issues in JSON format.
     *
     * @param request  the HttpServletRequest object
     * @param response the HttpServletResponse object used to send JSON data
     * @throws IOException if an I/O error occurs while writing the response
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ServletContext context = request.getServletContext();
        IssueManager manager = (IssueManager) context.getAttribute("IssueManager");

        List<IssueListRecord> issueList = manager.getAllIssueRecords();

        Gson gson = new Gson();
        String json = gson.toJson(issueList);

        response.getWriter().write(json);
    }

    public void destroy() {
    }
}
