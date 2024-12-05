/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.bookstore.controller.admin.book;

import com.bookstore.services.BookServices;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DELL
 */
@MultipartConfig(
                //Kích thước tối đa của một tệp được lưu trữ trong bộ nhớ là 10 KB.
                //Nếu tệp lớn hơn ngưỡng này, nó sẽ được ghi vào ổ đĩa tạm thời.
		fileSizeThreshold = 1024 * 10,	// 10 KB
                //Kích thước tối đa của mỗi tệp tải lên là 300 KB. 
                //Nếu kích thước tệp vượt quá giá trị này, việc tải lên sẽ bị từ chối.
		maxFileSize = 1024 * 300,		// 300 KB
                //Kích thước tối đa của toàn bộ yêu cầu (bao gồm tất cả tệp và dữ liệu khác) là 1 MB
		maxRequestSize = 1024 * 1024	// 1 MB 
)
@WebServlet(name = "CreateBookServlet", urlPatterns = {"/admin/create_book"})
public class CreateBookServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            //Tạo một đối tượng BookServices, truyền đối tượng request và response để quản lý dữ liệu và phản hồi.
            BookServices book  =new BookServices(request,response);
            //Gọi phương thức createBook
            book.createBook();
        } catch (ParseException ex) {
            Logger.getLogger(CreateBookServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
