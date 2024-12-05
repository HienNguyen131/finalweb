/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.bookstore.controller.frontend.shoppingcart;

import com.bookstore.entity.Book;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author DELL
 */
@WebServlet(name = "RemoveCartServlet", urlPatterns = {"/remove_from_cart"})
public class RemoveCartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Object cartShopping = request.getSession().getAttribute("cart");
        
        ShoppingCart cart = (ShoppingCart) cartShopping;
        
        Integer bookId = Integer.parseInt(request.getParameter("book_id"));
        cart.deleteBook(new Book(bookId));
        
        String url = request.getContextPath().concat("/view_cart");
        response.sendRedirect(url);
    }


}
