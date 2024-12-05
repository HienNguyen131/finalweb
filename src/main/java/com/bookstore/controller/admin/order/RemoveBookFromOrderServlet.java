/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.bookstore.controller.admin.order;

import com.bookstore.entity.BookOrder;
import com.bookstore.entity.OrderDetail;
import com.bookstore.services.CommonUtility;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author DELL
 */
@WebServlet(name = "RemoveBookFromOrderServlet", urlPatterns = {"/admin/remove_book_from_order"})
public class RemoveBookFromOrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer bookId = Integer.parseInt(request.getParameter("id"));
        HttpSession session = request.getSession();
        BookOrder order = (BookOrder) session.getAttribute("order");
        
        Set<OrderDetail> orderDetails = order.getOrderDetails();
        Iterator<OrderDetail> iterator = orderDetails.iterator();
        while(iterator.hasNext()){
            OrderDetail orderDetail = iterator.next();
            if(orderDetail.getBook().getBookId() == bookId){
                float newTotal = order.getTotal() - orderDetail.getSubtotal();
                order.setTotal(newTotal);
                iterator.remove();
            }
        }
                CommonUtility.generateCountryList(request);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("order_form.jsp");
        
        requestDispatcher.forward(request, response);
    }

}
