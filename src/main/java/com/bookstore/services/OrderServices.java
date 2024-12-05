/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.services;

import com.bookstore.controller.frontend.shoppingcart.ShoppingCart;
import com.bookstore.dao.OrderDAO;
import com.bookstore.entity.Book;
import com.bookstore.entity.BookOrder;
import com.bookstore.entity.Customer;
import com.bookstore.entity.OrderDetail;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.ShippingAddress;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author DELL
 */
public class OrderServices {

    private OrderDAO orderDAO;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public OrderServices(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        orderDAO = new OrderDAO();
    }

    public void listAll() throws ServletException, IOException {
        listAll(null);
    }

    private void listAll(String message) throws ServletException, IOException {
        List<BookOrder> listOrder = orderDAO.listAll();
        request.setAttribute("listOrder", listOrder);
        if (message != null) {
            request.setAttribute("message", message);
        }
        String url = "order_list.jsp";
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
        requestDispatcher.forward(request, response);

    }

    public void delete() throws ServletException, IOException {
        Integer orderId = Integer.parseInt(request.getParameter("id"));
        orderDAO.delete(orderId);
        String message = "The order with " + orderId + " has been deleted successfully!";
        listAll(message);
    }

    public void viewOrderDetailForAdmin() throws ServletException, IOException {
        Integer orderId = Integer.parseInt(request.getParameter("id"));
        BookOrder order = orderDAO.get(orderId);
        request.setAttribute("order", order);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("order_detail.jsp");
        requestDispatcher.forward(request, response);

    }

    public void showCheckOutForm() throws ServletException, ServletException, IOException {
        HttpSession session = request.getSession();
        ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("cart");

        // tax is 10% of subtotal
        float tax = shoppingCart.getTotalAmount() * 0.1f;

        // shipping fee is 1.0 USD per copy
        float shippingFee = shoppingCart.getTotalQuantity() * 1.0f;

        float total = shoppingCart.getTotalAmount() + tax + shippingFee;

        session.setAttribute("tax", tax);
        session.setAttribute("shippingFee", shippingFee);
        session.setAttribute("total", total);

        CommonUtility.generateCountryList(request);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/frontend/checkout.jsp");
        requestDispatcher.forward(request, response);
    }

    public void placeOrder() throws ServletException, ServletException, IOException {
        String paymentMethod = request.getParameter("paymentMethod");
        BookOrder order = readBookOrderInfo();
        if (paymentMethod.equals("paypal")) {
            PaymentServices payment = new PaymentServices(request, response);
            request.getSession().setAttribute("order4Paypal", order);
            payment.authorizePayment(order);
        } else {
            placeOrderCOD(order);
        }

    }
    public Integer placeOrderPaypal(Payment payment){
        		BookOrder order = (BookOrder) request.getSession().getAttribute("order4Paypal");
		ItemList itemList = payment.getTransactions().get(0).getItemList();
		ShippingAddress shippingAddress = itemList.getShippingAddress();
		String shippingPhoneNumber = itemList.getShippingPhoneNumber();
		
		String recipientName = shippingAddress.getRecipientName();
		String[] names = recipientName.split(" ");
		
		order.setFirstname(names[0]);
		order.setLastname(names[1]);
		order.setAddressLine1(shippingAddress.getLine1());
		order.setAddressLine2(shippingAddress.getLine2());
		order.setCity(shippingAddress.getCity());
		order.setState(shippingAddress.getState());
		order.setCountry(shippingAddress.getCountryCode());
		order.setPhone(shippingPhoneNumber);
		
		return saveOrder(order);
    }

    private BookOrder readBookOrderInfo() {
        String paymentMethod = request.getParameter("paymentMethod");
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String phone = request.getParameter("phone");
        String address1 = request.getParameter("address1");
        String address2 = request.getParameter("address2");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String zipcode = request.getParameter("zipcode");
        String country = request.getParameter("country");
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("loggedCustomer");
        BookOrder order = new BookOrder();
        order.setAddressLine1(address1);
        order.setAddressLine2(address2);
        order.setCity(city);
        order.setCountry(country);
        order.setPhone(phone);
        order.setState(state);
        order.setZipcode(zipcode);
        order.setFirstname(firstname);
        order.setLastname(lastname);
        order.setPaymentMethod(paymentMethod);
        order.setCustomer(customer);
        Set<OrderDetail> orderDetails = new HashSet<>();
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        Map<Book, Integer> items = cart.getItems();
        Iterator<Book> iterator = items.keySet().iterator();
        while (iterator.hasNext()) {
            Book book = iterator.next();
            int quantity = items.get(book);
            float subTotal = quantity * book.getPrice();

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setBook(book);
            orderDetail.setBookOrder(order);
            orderDetail.setQuantity(quantity);
            orderDetail.setSubtotal(subTotal);
            orderDetails.add(orderDetail);
        }
        order.setOrderDetails(orderDetails);
        float tax = (Float) session.getAttribute("tax");
        float shippingFee = (Float) session.getAttribute("shippingFee");
        float total = (Float) session.getAttribute("total");

        order.setSubtotal(cart.getTotalAmount());
        order.setTax(tax);
        order.setShippingFee(shippingFee);
        order.setTotal(total);
        return order;
    }
    

    private void placeOrderCOD(BookOrder order) throws ServletException, IOException {
        saveOrder(order);
        String message = "You have successfully ordered! Thank you!. You order has been recieved.We willl delivery your books within few days!";
        request.setAttribute("message", message);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/frontend/message.jsp");
        requestDispatcher.forward(request, response);
    }

    public void showAllOrder() throws ServletException, IOException {
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("loggedCustomer");

        List<BookOrder> listOrders = orderDAO.listByCustomer(customer.getCustomerId());
        request.setAttribute("listOrders", listOrders);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/frontend/order_list.jsp");
        requestDispatcher.forward(request, response);
    }

    public void showOrderDetailForCustomer() throws ServletException, IOException {
        Integer bookOrderId = Integer.parseInt(request.getParameter("id"));
        Customer customer = (Customer) request.getSession().getAttribute("loggedCustomer");
        Integer customerId = customer.getCustomerId();
        BookOrder order = orderDAO.get(bookOrderId, customerId);
        request.setAttribute("order", order);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/frontend/order_detail.jsp");
        requestDispatcher.forward(request, response);
    }

    public void showEditOrderForm() throws ServletException, IOException {
        Integer orderId = Integer.parseInt(request.getParameter("id"));
        CommonUtility.generateCountryList(request);

        HttpSession session = request.getSession();

        Object isPendingBook = session.getAttribute("NewBookPendingToAddToOrder");
        if (isPendingBook == null) {
            BookOrder order = orderDAO.get(orderId);
            session.setAttribute("order", order);
        } else {
            session.removeAttribute("NewBookPendingToAddToOrder");
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("order_form.jsp");
        requestDispatcher.forward(request, response);
    }

    public void updateOrder() throws ServletException, IOException {
        HttpSession session = request.getSession();
        BookOrder order = (BookOrder) session.getAttribute("order");

        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String phone = request.getParameter("phone");
        String address1 = request.getParameter("address1");
        String address2 = request.getParameter("address2");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String zipcode = request.getParameter("zipcode");
        String country = request.getParameter("country");

        float shippingFee = Float.parseFloat(request.getParameter("shippingFee"));
        float tax = Float.parseFloat(request.getParameter("tax"));

        String paymentMethod = request.getParameter("paymentMethod");
        String orderStatus = request.getParameter("orderStatus");

        order.setFirstname(firstname);
        order.setLastname(lastname);
        order.setPhone(phone);
        order.setAddressLine1(address1);
        order.setAddressLine2(address2);
        order.setCity(city);
        order.setState(state);
        order.setZipcode(zipcode);
        order.setCountry(country);
        order.setShippingFee(shippingFee);
        order.setTax(tax);
        order.setPaymentMethod(paymentMethod);
        order.setStatus(orderStatus);

        String[] arrayBookId = request.getParameterValues("bookId");
        String[] arrayPrice = request.getParameterValues("price");
        String[] arrayQuantity = new String[arrayBookId.length];

        for (int i = 1; i <= arrayQuantity.length; i++) {
            arrayQuantity[i - 1] = request.getParameter("quantity" + i);
        }

        Set<OrderDetail> orderDetails = order.getOrderDetails();
        orderDetails.clear();

        float totalAmount = 0.0f;

        for (int i = 0; i < arrayBookId.length; i++) {
            int bookId = Integer.parseInt(arrayBookId[i]);
            int quantity = Integer.parseInt(arrayQuantity[i]);
            float price = Float.parseFloat(arrayPrice[i]);

            float subtotal = price * quantity;

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setBook(new Book(bookId));
            orderDetail.setQuantity(quantity);
            orderDetail.setSubtotal(subtotal);
            orderDetail.setBookOrder(order);

            orderDetails.add(orderDetail);

            totalAmount += subtotal;
        }

        order.setSubtotal(totalAmount);
        totalAmount += shippingFee;
        totalAmount += tax;

        order.setTotal(totalAmount);

        orderDAO.update(order);

        String message = "The order " + order.getOrderId() + " has been updated successfully";

        listAll(message);

    }

    private Integer saveOrder(BookOrder order) {
        BookOrder savedOrder = orderDAO.create(order);
        ShoppingCart cart = (ShoppingCart) request.getSession().getAttribute("cart");
        cart.clear();
        return savedOrder.getOrderId();
    }

}
