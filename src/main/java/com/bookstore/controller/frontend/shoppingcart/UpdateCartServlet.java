/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.bookstore.controller.frontend.shoppingcart;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 *
 * @author DELL
 */
@WebServlet(name = "UpdateCartServlet", urlPatterns = {"/update_cart"})
public class UpdateCartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] aBookId = request.getParameterValues("bookId");
                    String[] aQuantity = new String[aBookId.length];
        int[] toArrayId = Arrays.stream(aBookId).mapToInt(Integer::parseInt).toArray();
        for(int i=1;i<=aBookId.length;i++){
            String is = request.getParameter("quantity"+i);
            aQuantity[i-1]=is;
            
        }
        int[] toArrayQuantity = Arrays.stream(aQuantity).mapToInt(Integer::parseInt).toArray();
        Object cartShop = request.getSession().getAttribute("cart");
        ShoppingCart cart = (ShoppingCart) cartShop;
        cart.updateCart(toArrayId, toArrayQuantity);
        String url = request.getContextPath().concat("/view_cart");
        response.sendRedirect(url);
    }

}
