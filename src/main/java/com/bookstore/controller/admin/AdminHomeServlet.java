/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.bookstore.controller.admin;

import com.bookstore.dao.BookDAO;
import com.bookstore.dao.CustomerDAO;
import com.bookstore.dao.OrderDAO;
import com.bookstore.dao.ReviewDAO;
import com.bookstore.dao.UserDAO;
import com.bookstore.entity.BookOrder;
import com.bookstore.entity.Review;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author DELL
 */
@WebServlet(name = "AdminHomeServlet", urlPatterns = {"/admin/"})
public class AdminHomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        OrderDAO order = new OrderDAO();
        UserDAO user = new UserDAO();
        BookDAO book = new BookDAO();
                ReviewDAO review = new ReviewDAO();
        CustomerDAO customer  = new CustomerDAO();
        long totalBooks = book.count();
        long totalUsers = user.count();
        long totalCustomers = customer.count();
        long totalOrders = order.count();
        long totalReviews = review.count();
        request.setAttribute("totalBooks", totalBooks);
        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("totalReviews", totalReviews);
        request.setAttribute("totalCustomers", totalCustomers);
        request.setAttribute("totalOrders", totalOrders);
        List<BookOrder> listMostRecentSales = order.listRecentSale();
        request.setAttribute("listMostRecentSales",listMostRecentSales);
        List<Review> listMostRecentReviews = review.listRecentReview();
        request.setAttribute("listMostRecentReviews", listMostRecentReviews);
        RequestDispatcher requestDis = request.getRequestDispatcher("index.jsp");
        requestDis.forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request,response);
    }

}
