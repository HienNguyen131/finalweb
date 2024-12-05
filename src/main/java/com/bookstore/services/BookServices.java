package com.bookstore.services;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.bookstore.dao.BookDAO;
import com.bookstore.dao.CategoryDAO;
import com.bookstore.entity.Book;
import com.bookstore.entity.Category;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

public class BookServices {

    private BookDAO bookDAO;
    private CategoryDAO categoryDAO;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public BookServices(HttpServletRequest request, HttpServletResponse response) {
        super();
        this.request = request;
        this.response = response;
        bookDAO = new BookDAO();
        categoryDAO = new CategoryDAO();
    }

    public void listBooks() throws ServletException, IOException {
        listBooks(null);
    }

    public void listBooks(String message) throws ServletException, IOException {
        if (!response.isCommitted()) {
            List<Book> listBooks = bookDAO.listAll();
            request.setAttribute("listBooks", listBooks);

            if (message != null) {
                request.setAttribute("message", message);
            }

            String listPage = "book_list.jsp";
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(listPage);
            requestDispatcher.forward(request, response);
        } else {
            // Handle the case when the response is already committed
            System.out.println("Response already committed!");
        }
    }

    public void showBookNewForm() throws ServletException, IOException {
        List<Category> listCategory = categoryDAO.listAll();
        request.setAttribute("listCategory", listCategory);
        request.setAttribute("pageTitle", "Create New Book");

        String newPage = "book_form.jsp";
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(newPage);
        requestDispatcher.forward(request, response);
    }

    public void createBook() throws ServletException, IOException, ParseException {
        String title = request.getParameter("title");

        Book existBook = bookDAO.findByTitle(title);

        if (existBook != null) {
            String message = "Could not create new book because the title '"
                    + title + "' already exists.";
            listBooks(message);
            return;
        }

        Book newBook = new Book();
        readBookFields(newBook);

        Book createdBook = bookDAO.create(newBook);

        if (createdBook.getBookId() > 0) {
            String message = "A new book has been created successfully.";
            listBooks(message);
        }
    }

    public void readBookFields(Book book) throws ServletException, IOException, java.text.ParseException {
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String description = request.getParameter("description");
        String isbn = request.getParameter("isbn");
        ////Nếu không có giá trị, price mặc định là 100.
        float price = 100;
        //ep kieu float
        price = Float.parseFloat(request.getParameter("price"));

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date publishDate = null;

        try {
            publishDate = dateFormat.parse(request.getParameter("publishDate"));
        } catch (ParseException ex) {
            ex.printStackTrace();
            throw new ServletException("Error parsing publish date (format is yyyy-MM-dd)");
        }

        book.setTitle(title);
        book.setAuthor(author);
        book.setDescription(description);
        book.setIsbn(isbn);
        book.setPublishDate(publishDate);

        Integer categoryId = Integer.parseInt(request.getParameter("category"));
        //Lấy đối tượng Category tương ứng với ID từ cơ sở dữ liệu thông qua categoryDAO.
        Category category = categoryDAO.get(categoryId);
        //Gán danh mục cho đối tượng Book
        book.setCategory(category);

        book.setPrice(price);
        //Lấy tệp tải lên từ form với trường bookImage.
        Part part = request.getPart("bookImage");

        if (part != null && part.getSize() > 0) {//Nếu tệp tồn tại và kích thước lớn hơn 0:
            long size = part.getSize();
            byte[] imageBytes = new byte[(int) size];
            //InputStream cung cấp các phương thức cơ bản để đọc từng byte, một mảng byte, hoặc các luồng byte từ nguồn dữ liệu
            InputStream inputStream = part.getInputStream();
            //Đọc dữ liệu tệp thành một mảng byte (imageBytes).
            inputStream.read(imageBytes);
            
            inputStream.close();
            //Gán dữ liệu hình ảnh vào thuộc tính image của đối tượng Book
            book.setImage(imageBytes);
        }

    }

    public void editBook() throws ServletException, IOException {
        try {
            Integer bookId = Integer.parseInt(request.getParameter("id"));
            Book book = bookDAO.get(bookId);
            List<Category> listCategory = categoryDAO.listAll();

            if (book == null) {
                // Xử lý khi book không tồn tại
                String errorMessage = "The book with ID " + bookId + " does not exist.";
                request.setAttribute("message", errorMessage);
                listBooks(errorMessage);
                return;
            }

            // Đặt các thuộc tính cho request
            request.setAttribute("book", book);
            request.setAttribute("listCategory", listCategory);

            // Chuyển hướng tới form chỉnh sửa
            String editPage = "book_form.jsp";
            //RequestDispatcher để chuyển hướng yêu cầu và phản hồi đến trang JSP.
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(editPage);
            requestDispatcher.forward(request, response);
        } catch (NumberFormatException e) {
            // Xử lý lỗi định dạng ID
            String errorMessage = "Invalid book ID.";
            request.setAttribute("message", errorMessage);
            listBooks(errorMessage);
        } catch (Exception e) {
            // Log lỗi và hiển thị thông báo lỗi
            e.printStackTrace();
            String errorMessage = "An error occurred while editing the book.";
            request.setAttribute("message", errorMessage);
            listBooks(errorMessage);
        }
    }

    public void updateBook() throws ServletException, IOException, ParseException {
        Integer bookId = Integer.parseInt(request.getParameter("bookId"));
        String title = request.getParameter("title");

        Book existBook = bookDAO.get(bookId);
        Book bookByTitle = bookDAO.findByTitle(title);

        if (bookByTitle != null && !existBook.equals(bookByTitle)) {
            String message = "Could not update book because there's another book having same title.";
            listBooks(message);
            return;
        }

        readBookFields(existBook);

        bookDAO.update(existBook);

        String message = "The book has been updated successfully.";
        listBooks(message);
    }

    public void deleteBook() throws ServletException, IOException {
        Integer bookId = Integer.parseInt(request.getParameter("id"));

        bookDAO.delete(bookId);

        String message = "The book has been deleted successfully.";
        listBooks(message);
    }

    public void listBooksByCategory() throws ServletException, IOException {
        int categoryId = Integer.parseInt(request.getParameter("id"));
        List<Book> listBooks = bookDAO.listByCategory(categoryId);
        Category category = categoryDAO.get(categoryId);

        request.setAttribute("listBooks", listBooks);
        request.setAttribute("category", category);

        String listPage = "frontend/books_list_by_category.jsp";
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(listPage);
        requestDispatcher.forward(request, response);
    }

    public void viewBookDetail() throws ServletException, IOException {
        Integer bookId = Integer.parseInt(request.getParameter("id"));
        Book book = bookDAO.get(bookId);

        request.setAttribute("book", book);

        String detailPage = "frontend/book_detail.jsp";
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(detailPage);
        requestDispatcher.forward(request, response);
    }

    public void search() throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        List<Book> result = null;

        if (keyword.equals("")) {
            result = bookDAO.listAll();
        } else {
            result = bookDAO.search(keyword);
        }

        request.setAttribute("keyword", keyword);
        request.setAttribute("result", result);

        String resultPage = "frontend/search_result.jsp";
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(resultPage);
        requestDispatcher.forward(request, response);
    }
}
