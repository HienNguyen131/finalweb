/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Filter.java to edit this template
 */
package com.bookstore.controller.admin;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author DELL
 */
@WebFilter(filterName = "AdminLoginFilter", urlPatterns = {"/admin/*"})
public class AdminLoginFilter implements Filter {


    public AdminLoginFilter() {
    }

    public void destroy() {
    }


    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) sr;
        HttpSession session = httpRequest.getSession(false);
        boolean loggedIn = session != null && session.getAttribute("useremail") != null;
        String loginURI = httpRequest.getContextPath() + "/admin/login";
        boolean logginRequest = httpRequest.getRequestURI().equals(loginURI);
        boolean logginPage = httpRequest.getRequestURI().endsWith("sign_in.jsp");
        if (loggedIn && (logginRequest || logginPage)) {
            RequestDispatcher requestDis = httpRequest.getRequestDispatcher("/admin/");
            requestDis.forward(sr, sr1);

        } else if (loggedIn || logginRequest) {
            fc.doFilter(sr, sr1);
        } else {
            RequestDispatcher requestDis = httpRequest.getRequestDispatcher("login.jsp");
            requestDis.forward(sr, sr1);
        }
    }
        /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {

    }

}
