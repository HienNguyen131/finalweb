/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.bookstore.controller.frontend.order;

import com.bookstore.services.OrderServices;
import com.bookstore.services.PaymentServices;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.ShippingAddress;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DELL
 */
@WebServlet(name = "ExecutePaymentServlet", urlPatterns = {"/execute_payment"})
public class ExecutePaymentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PaymentServices pay = new PaymentServices(request,response);
        try {
            Payment payment = pay.executePayemnt();
            OrderServices order = new OrderServices(request,response);
            Integer orderId = order.placeOrderPaypal(payment);
            HttpSession session = request.getSession();
            session.setAttribute("orderId", orderId);
            
                   PayerInfo payerInfo = payment.getPayer().getPayerInfo();
            Transaction transaction = payment.getTransactions().get(0);


            request.setAttribute("payer", payerInfo);
            request.setAttribute("transaction", transaction);

            String reviewPage = "frontend/payment_receipt.jsp";
            request.getRequestDispatcher(reviewPage).forward(request, response);

        } catch (PayPalRESTException | IOException e) {
            e.printStackTrace();
            throw new ServletException("Error in getting payment details from PayPal.");
        }
    }
}


