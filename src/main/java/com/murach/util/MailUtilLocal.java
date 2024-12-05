package com.murach.util;

import jakarta.mail.Address;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class MailUtilLocal {

    public static void sendMail(String to, String from,
            String subject, String body, boolean bodyIsHTML)
            throws MessagingException {
        
        // Cấu hình SMTP cho Gmail với STARTTLS
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");  // Cổng STARTTLS
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");  // Bật STARTTLS
        props.put("mail.smtp.quitwait", "false");

        // Tạo Session với xác thực
        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);  // Bật chế độ Debug để theo dõi chi tiết

        // 2 - Tạo message
        Message message = new MimeMessage(session);
        message.setSubject(subject);
        if (bodyIsHTML) {
            message.setContent(body, "text/html");
        } else {
            message.setText(body);
        }

        // 3 - Địa chỉ người gửi và người nhận
        Address fromAddress = new InternetAddress(from);
        Address toAddress = new InternetAddress(to);
        message.setFrom(fromAddress);
        message.setRecipient(Message.RecipientType.TO, toAddress);

        // 4 - Gửi email với mật khẩu ứng dụng
        Transport transport = session.getTransport("smtp");
        transport.connect("nguyenhuuductho0411@gmail.com", "zmob wowz uuiq hqmn"); // Thay "your-app-password" với mật khẩu ứng dụng
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
}
