package servlets;

import database.DB_Connection;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet(name = "Rental", value = "/Rental")
public class Rental extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DB_Connection db = new DB_Connection();
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String paymentAmountStr = request.getParameter("paymentAmount");
        String status = request.getParameter("status");
        String customerID = request.getParameter("customerID");
        String carMotorbikeId =request.getParameter("carMotorbikeId");
        String scooterBikeId =request.getParameter("scooterBikeId");

        try {
            Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateStr);
            Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDateStr);
            double paymentAmount = Double.parseDouble(paymentAmountStr);

            insertRental(startDate, endDate, paymentAmount, status);

            response.setContentType("text/plain");
            PrintWriter out = response.getWriter();
            out.println("Rental record inserted successfully!");
        } catch (ParseException | NumberFormatException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid data format");
        }

    }

    private void insertRental(Date startDate, Date endDate, double paymentAmount, String status) {
        DB_Connection db = new DB_Connection();

        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish a connection
            try (Connection con = db.getConnection()) {
                // Create a prepared statement and execute an INSERT SQL query
                String sql = "INSERT INTO Rental (StartDate, EndDate, PaymentAmount, Status) VALUES (?, ?, ?, ?)";
                try (PreparedStatement statement = con.prepareStatement(sql)) {
                    statement.setDate(1, new java.sql.Date(startDate.getTime()));
                    statement.setDate(2, new java.sql.Date(endDate.getTime()));
                    statement.setDouble(3, paymentAmount);
                    statement.setString(4, status);

                    statement.executeUpdate();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

}
