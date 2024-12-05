/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.bookstore.controller.admin.order;

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
import java.util.List;

/**
 *
 * @author DELL
 */
@WebServlet(name = "ShowAddBookFormServlet", urlPatterns = {"/admin/add_book_form"})
public class ShowAddBookFormServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        BookDAO book = new BookDAO();
        List<Book> listBook = book.listAll();
        request.setAttribute("listBook", listBook);
        
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("add_book_form.jsp");
        requestDispatcher.forward(request, response);
    }

}
