/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.bookstore.controller.frontend.shoppingcart;

import com.bookstore.dao.BookDAO;
import com.bookstore.entity.Book;
import jakarta.servlet.RequestDispatcher;
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
@WebServlet(name = "AddToCartServlet", urlPatterns = {"/add_to_cart"})
public class AddToCartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer bookId = Integer.parseInt(request.getParameter("book_id"));

        Object cartShop = request.getSession().getAttribute("cart");
        ShoppingCart cart = null;
        if (cartShop != null &&  cartShop instanceof ShoppingCart) {
                        cart = (ShoppingCart) cartShop;

        } else{
            cart = new ShoppingCart();
            request.getSession().setAttribute("cart", cart);
        }
        BookDAO bookDAO = new BookDAO();
        Book book = bookDAO.get(bookId);
        cart.addBook(book);
        
        String url = request.getContextPath().concat("/view_cart");
        response.sendRedirect(url);
    }

}
