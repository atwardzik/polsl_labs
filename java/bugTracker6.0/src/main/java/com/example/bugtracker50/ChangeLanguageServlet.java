package com.example.bugtracker50;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * ChangeLanguageServlet handles requests to change the user's language preference.
 * It sets a "lang" cookie with the selected language, defaulting to "en" if none is provided.
 *
 * @author Artur Twardzik
 * @version 0.5
 */
@WebServlet("/change-language")
public class ChangeLanguageServlet extends HttpServlet {
    public void init() {
    }

    /**
     * Processes both GET and POST requests to update the language cookie.
     *
     * @param request  the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws IOException if an I/O error occurs while setting the cookie
     */
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