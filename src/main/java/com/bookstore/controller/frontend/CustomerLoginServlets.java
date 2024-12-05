/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Filter.java to edit this template
 */
package com.bookstore.controller.frontend;

import jakarta.persistence.Query;
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

/**
 *
 * @author DELL
 */
@WebFilter(filterName = "CustomerLoginServlets", urlPatterns = {"/*"})
public class CustomerLoginServlets implements Filter {
    private static final String[] loginRequiredURLs = {
            "/view_profile","/edit_profile","/update_profile","/write_review","/checkout","/place_order","/view_orders","/show_order_detail"
    };
    public CustomerLoginServlets() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) sr;
        HttpSession session = request.getSession(false);
        String path = request.getRequestURI().substring(request.getContextPath().length());
        if (path.startsWith("/admin/")) {
            fc.doFilter(sr, sr1);
        }
        String loginURL = request.getRequestURL().toString();
        boolean t = session != null && session.getAttribute("loggedCustomer")!=null;
        if(!t && isLoginRequired( loginURL)){
            String url ="frontend/login.jsp";
            String query = request.getQueryString();
            if(query!= null){
                loginURL = loginURL.concat("?").concat(query);
            }
            request.getSession().setAttribute("loginURL",loginURL);
            RequestDispatcher requestDis = request.getRequestDispatcher(url);
            requestDis.forward(sr, sr1);
        }
        else{
            fc.doFilter(sr, sr1);
        }
    }
    private Boolean isLoginRequired(String loginURL){
        for(String  loginRequiredURL :  loginRequiredURLs){
            if(loginURL.contains( loginRequiredURL)){
                return true;
            }
        }
        return false;
    }

        @Override
        public void destroy
        
            () {
        Filter.super.destroy(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        }

    }
