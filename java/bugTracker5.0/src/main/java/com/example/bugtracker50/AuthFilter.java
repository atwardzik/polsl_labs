package com.example.bugtracker50;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * AuthFilter is a servlet filter that intercepts requests to protected URLs
 * such as "/edit-issue", "/new-issue", and "/user-details" to ensure that
 * the user is authenticated before accessing these resources.
 * <p>
 * If a user is not logged in (i.e., there is no session or no "userID"
 * attribute in the session), this filter sends an HTTP 401 Unauthorized
 * response.
 * </p>
 *
 * @author Artur Twardzik
 * @version 0.5
 */
@WebFilter(urlPatterns = {
        "/edit-issue",
        "/new-issue",
        "/user-details"
})
public class AuthFilter implements Filter {

    /**
     * Filters incoming HTTP requests and checks for user authentication.
     * If the session does not exist or does not contain a "userID" attribute,
     * the method sends an HTTP 401 Unauthorized error and does not proceed further.
     * Otherwise, it forwards the request along the filter chain.
     *
     * @param servletRequest  the ServletRequest object, expected to be an HttpServletRequest
     * @param servletResponse the ServletResponse object, expected to be an HttpServletResponse
     * @param filterChain     the FilterChain for invoking the next filter or the target resource
     * @throws IOException      if an I/O error occurs during request processing
     * @throws ServletException if a servlet-specific error occurs during request processing
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userID") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not logged in");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
