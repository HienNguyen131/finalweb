/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.services;

import com.bookstore.dao.BookDAO;
import com.bookstore.dao.ReviewDAO;
import com.bookstore.entity.Book;
import com.bookstore.entity.Customer;
import com.bookstore.entity.Review;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.List;

/**
 *
 * @author DELL
 */
public class ReviewServices {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ReviewDAO reviewDAO;
    private BookDAO bookDAO;

    public ReviewServices(HttpServletRequest request,HttpServletResponse response) {
        super();
        this.request = request;
        this.response = response;
        reviewDAO = new ReviewDAO();
        bookDAO = new BookDAO();
    }
    public void listAllReview() throws ServletException, IOException{
        listAllReview(null);
    }
    public void listAllReview(String message) throws ServletException, IOException{
        List<Review> listReviews = reviewDAO.listAll();
        request.setAttribute("listReviews", listReviews);
        if(message!=null){
            request.setAttribute("message", message);
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("review_list.jsp");
        requestDispatcher.forward(request, response);
        
    }

    public void editReview() throws ServletException, IOException {
        Integer reviewId = Integer.parseInt(request.getParameter("id"));
        Review review = reviewDAO.get(reviewId);
        request.setAttribute("review", review);
        
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("review_form.jsp");
        requestDispatcher.forward(request, response);
    }

    public void updateReview() throws ServletException, IOException {
        Integer reviewId = Integer.parseInt(request.getParameter("reviewId"));
        Review review = reviewDAO.get(reviewId);
        String headline = request.getParameter("headline");
        String comment = request.getParameter("comment");
        
        review.setHeadline(headline);
        review.setComment(comment);
        reviewDAO.update(review);
        String message="You have successfully updated review";
        listAllReview(message);
    }

    public void deleteReview() throws IOException, ServletException {
        Integer reviewId = Integer.parseInt(request.getParameter("id"));

        reviewDAO.delete(reviewId);
        String message = "You have delete review successfully!";
        listAllReview(message);
    }

    public void showReviewForm() throws ServletException, IOException {
        Integer bookId = Integer.parseInt(request.getParameter("book_id"));
        Book book = bookDAO.get(bookId);
        request.setAttribute("book", book);
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("loggedCustomer");
        Review review = reviewDAO.findByCustomerAndBook(customer.getCustomerId(), bookId);
        String url;
        if(review!=null){
            request.setAttribute("review",review);
            url="/frontend/review_info.jsp";
        }
        else{
            url="/frontend/review_form.jsp";
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
        requestDispatcher.forward(request, response);
    }

    public void submitReview() throws ServletException, IOException { 
        Integer bookId = Integer.parseInt(request.getParameter("bookId"));
        Book book = bookDAO.get(bookId);
        String headline = request.getParameter("headline");
        String comment = request.getParameter("comment");
        Integer rating =Integer.parseInt(request.getParameter("rating"));
        Review review = new Review();
        review.setComment(comment);
        review.setRating(rating);
        review.setHeadline(headline);
        review.setBook(book);
        
        Customer customer  = (Customer) request.getSession().getAttribute("loggedCustomer");
        review.setCustomer(customer);
        review.setReviewTime(new Date());
        reviewDAO.create(review);
        request.setAttribute("book", book);
        String message="/frontend/review_done.jsp";
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(message);
        requestDispatcher.forward(request, response);
    }
    
}
