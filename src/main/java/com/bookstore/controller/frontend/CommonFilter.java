/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Filter.java to edit this template
 */
package com.bookstore.controller.frontend;

import com.bookstore.dao.CategoryDAO;
import com.bookstore.entity.Category;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * @author DELL
 */
@WebFilter(filterName = "CommonFilter", urlPatterns = {"/*"})
public class CommonFilter implements Filter {

    private CategoryDAO categoryDAO;

    public CommonFilter() {
        categoryDAO = new CategoryDAO();
    }
	public void destroy() {
	}


    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) sr;
        String path = request.getRequestURI().substring(request.getContextPath().length());
        if (!path.startsWith("/admin/")) {
            List<Category> listCategory = categoryDAO.listAll();
            sr.setAttribute("listCategory", listCategory);
        }

        fc.doFilter(sr, sr1);
    }
	public void init(FilterConfig fConfig) throws ServletException {
	}


}
