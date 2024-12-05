/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.bookstore.controller.frontend;

import com.bookstore.dao.BookDAO;
import com.bookstore.dao.CategoryDAO;
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
@WebServlet(name = "HomeServlet", urlPatterns = {""})
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CategoryDAO category = new CategoryDAO();
        BookDAO bookDAO = new BookDAO();
        List<Book> listNewPublishBook = bookDAO.listNewBooks();
        request.setAttribute("listNewPublishBook", listNewPublishBook);
                List<Book> listBestSellingBooks = bookDAO.listByBestSellingBook();
        request.setAttribute("listBestSellingBooks", listBestSellingBooks);
        List<Book> listFavoredBooks = bookDAO.listMostFavoredBooks();
        request.setAttribute("listFavoredBooks", listFavoredBooks);
        String url = "/frontend/index.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(url);
        dispatcher.forward(request, response);
    }

}


