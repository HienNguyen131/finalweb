package com.bookstore.services;

import java.io.IOException;

import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.bookstore.dao.CustomerDAO;
import com.bookstore.entity.Customer;
import com.murach.util.MailUtilLocal;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;

public class CustomerServices {

    private CustomerDAO customerDAO;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public CustomerServices(HttpServletRequest request, HttpServletResponse response) {
        super();
        this.request = request;
        this.response = response;
        this.customerDAO = new CustomerDAO();
    }

    public void listCustomers(String message) throws ServletException, IOException {
        List<Customer> listCustomer = customerDAO.listAll();

        if (message != null) {
            request.setAttribute("message", message);
        }

        request.setAttribute("listCustomer", listCustomer);

        String listPage = "customer_list.jsp";
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(listPage);
        requestDispatcher.forward(request, response);
    }

    public void listCustomers() throws ServletException, IOException {
        listCustomers(null);
    }

    public void createCustomer() throws ServletException, IOException {
        //Chuẩn bị danh sách các quốc gia (có thể từ một nguồn cố định hoặc cơ sở dữ liệu).
        CommonUtility.generateCountryList(request);
        String email = request.getParameter("email");
        Customer existCustomer = customerDAO.findByEmail(email);

        if (existCustomer != null) {
            String message = "Could not create new customer: the email "
                    + email + " is already registered by another customer.";
            listCustomers(message);

        } else {
            Customer newCustomer = new Customer();
            updateCustomerFieldsFromForm(newCustomer);
            customerDAO.create(newCustomer);

            String message = "New customer has been created successfully.";
            listCustomers(message);

        }

    }

    private void updateCustomerFieldsFromForm(Customer customer) {
        String email = request.getParameter("email");
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String addressLine1 = request.getParameter("address1");
        String addressLine2 = request.getParameter("address2");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String zipCode = request.getParameter("zipCode");
        String country = request.getParameter("country");

        if (email != null && !email.equals("")) {
            customer.setEmail(email);
        }

        customer.setFirstname(firstname);
        customer.setLastname(lastname);

        if (password != null && !password.equals("")) {
            customer.setPassword(password);
        }

        customer.setPhone(phone);
        customer.setAddressLine1(addressLine1);
        customer.setAddressLine2(addressLine2);
        customer.setCity(city);
        customer.setState(state);
        customer.setZipcode(zipCode);
        customer.setCountry(country);
    }

    public void registerCustomer() throws ServletException, IOException {
        String email = request.getParameter("email");
        String firstname = request.getParameter("firstname");
        Customer existCustomer = customerDAO.findByEmail(email);
        String message = "";

        if (existCustomer != null) {
            message = "Could not register. The email "
                    + email + " is already registered by another customer.";
        } else {

            Customer newCustomer = new Customer();
            updateCustomerFieldsFromForm(newCustomer);
            customerDAO.create(newCustomer);

            message = "You have registered successfully! Thank you.<br/>"
                    + "<a href='login'>Click here</a> to login";

            String to = email;
            String from = "nguyenhuuductho0411@gmail.com";
            String subject = "Welcome to becoming a customer in our BookStoreWebistie";
            String body = "Dear " + firstname + ",\n\n"
                    + "Thanks for joining our BookStoreWebsite. We'll make sure to send "
                    + "you announcements about new products and promotions.\n"
                    + "Have a great day and thanks again!\n\n"
                    + "Nguyen Huu Duc Tho\n"
                    + "THQT Web Project\n"
                    + "From THQT BOOKSTORE";
            boolean isBodyHTML = false;

            try {
                MailUtilLocal.sendMail(to, from, subject, body, isBodyHTML);
//                MailUtilGmail.sendMail(to,from,subject,body,isBodyHTML);
            } catch (MessagingException e) {
                String errorMessage
                        = "ERROR: Unable to send email. "
                        + "Check Tomcat logs for details.<br>"
                        + "NOTE: You may need to configure your system "
                        + "as described in chapter 14.<br>"
                        + "ERROR MESSAGE: " + e.getMessage();
                request.setAttribute("errorMessage", errorMessage);
            }

        }

        String messagePage = "frontend/message.jsp";
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(messagePage);
        request.setAttribute("message", message);
        requestDispatcher.forward(request, response);
    }

    public void editCustomer() throws ServletException, IOException {

        Integer customerId = Integer.parseInt(request.getParameter("id"));
        Customer customer = customerDAO.get(customerId);

        request.setAttribute("customer", customer);

        CommonUtility.generateCountryList(request);

        String editPage = "customer_form.jsp";
        CommonUtility.generateCountryList(request);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(editPage);
        requestDispatcher.forward(request, response);
    }

    public void updateCustomer() throws ServletException, IOException {
        Integer customerId = Integer.parseInt(request.getParameter("customerId"));
        String email = request.getParameter("email");

        Customer customerByEmail = customerDAO.findByEmail(email);
        String message = null;

        if (customerByEmail != null && customerByEmail.getCustomerId() != customerId) {
            message = "Could not update the customer ID " + customerId
                    + "because there's an existing customer having the same email.";

        } else {

            Customer customerById = customerDAO.get(customerId);
            updateCustomerFieldsFromForm(customerById);

            customerDAO.update(customerById);

            message = "The customer has been updated successfully.";
        }

        listCustomers(message);
    }

    public void deleteCustomer() throws ServletException, IOException {
        Integer customerId = Integer.parseInt(request.getParameter("id"));
        customerDAO.delete(customerId);

        String message = "The customer has been deleted successfully.";
        listCustomers(message);
    }

    public void showLogin() throws ServletException, IOException {
        String loginPage = "frontend/login.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(loginPage);
        dispatcher.forward(request, response);
    }

    public void doLogin() throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Customer customer = customerDAO.checkLogin(email, password);

        if (customer == null) {
            String message = "Login failed. Please check your email and password.";
            request.setAttribute("message", message);
            showLogin();

        } else {
            HttpSession session = request.getSession();
            session.setAttribute("loggedCustomer", customer);
            Object redirectURL = session.getAttribute("loginURL");
            if (redirectURL != null) {
                String url = (String) redirectURL;
                session.removeAttribute("redirectURL");
                response.sendRedirect(url);
            } else {
                showCustomerProfile();
            }
        }
    }

    public void showCustomerProfile() throws ServletException, IOException {
        String profilePage = "frontend/customer_profile.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(profilePage);
        dispatcher.forward(request, response);
    }

    public void showCustomerProfileEditForm() throws ServletException, IOException {
        CommonUtility.generateCountryList(request);
        String editPage = "frontend/edit_profile.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(editPage);
        dispatcher.forward(request, response);
    }

    public void updateCustomerProfile() throws ServletException, IOException {
        Customer customer = (Customer) request.getSession().getAttribute("loggedCustomer");
        updateCustomerFieldsFromForm(customer);
        customerDAO.update(customer);
        showCustomerProfile();
    }

    public void newCustomer() throws ServletException, IOException {
        CommonUtility.generateCountryList(request);

        String customerForm = "customer_form.jsp";
        request.getRequestDispatcher(customerForm).forward(request, response);

    }

    public void showCustomerRegistrationForm() throws ServletException, IOException {
        CommonUtility.generateCountryList(request);

        String registerForm = "frontend/register_form.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(registerForm);
        dispatcher.forward(request, response);
    }
}
