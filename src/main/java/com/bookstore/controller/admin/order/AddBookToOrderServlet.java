/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.bookstore.controller.admin.order;

import com.bookstore.dao.BookDAO;
import com.bookstore.entity.Book;
import com.bookstore.entity.BookOrder;
import com.bookstore.entity.OrderDetail;
import com.bookstore.services.OrderServices;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author DELL
 */
@WebServlet(name = "AddBookToOrderServlet", urlPatterns = {"/admin/add_book_to_order"})
public class AddBookToOrderServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer bookId = Integer.parseInt(request.getParameter("bookId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        BookDAO bookDAO = new BookDAO();
        Book book = bookDAO.get(bookId);
        HttpSession session = request.getSession();
        BookOrder order = (BookOrder) session.getAttribute("order");
        
        float subTotal = book.getPrice()*quantity;
        
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setBook(book);
        orderDetail.setQuantity(quantity);
        orderDetail.setSubtotal(subTotal);
        
        order.setTotal(order.getTotal()+ subTotal);
        order.getOrderDetails().add(orderDetail);
        request.setAttribute("book", book);
        		session.setAttribute("NewBookPendingToAddToOrder", true);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("add_book_result.jsp");
        requestDispatcher.forward(request, response);
        

        
    }

}
