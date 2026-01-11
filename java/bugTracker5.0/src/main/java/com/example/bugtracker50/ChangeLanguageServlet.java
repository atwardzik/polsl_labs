package com.example.bugtracker50;

import com.google.gson.Gson;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.IssueListRecord;
import model.IssueManager;

import java.io.IOException;
import java.util.List;

@WebServlet("/change-language")
public class ChangeLanguageServlet extends HttpServlet {
    public void init() {
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String lang = request.getParameter("lang");
        if (lang == null) {
            lang = "en";
        }

        Cookie languageCookie = new Cookie("lang", lang);
        languageCookie.setPath("/");
        languageCookie.setMaxAge(60 * 60 * 24 * 365);
        response.addCookie(languageCookie);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    public void destroy() {
    }
}