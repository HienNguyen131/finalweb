# Image Tomcat với JDK 17
FROM tomcat:10.0-jdk17

# Xóa ứng dụng mặc định (ROOT)
RUN rm -rf /usr/local/tomcat/webapps/ROOT/*

# Sao chép file WAR vào thư mục webapps của Tomcat
COPY target/BookStoreWebsite-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/BookStoreWebsite.war

# Mở cổng 8080
EXPOSE 8080

# Lệnh khởi động Tomcat
CMD ["catalina.sh", "run"]