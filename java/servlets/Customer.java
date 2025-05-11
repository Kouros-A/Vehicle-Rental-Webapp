package servlets;

import database.DB_Connection;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Types;


@WebServlet(name = "Customer", value = "/Customer")
public class Customer extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            DB_Connection db = new DB_Connection();

            // Retrieve form data
            /*
            String email = request.getParameter("email");
            String description = request.getParameter("description");
            */
            String firstname = request.getParameter("firstname");
            String lastname = request.getParameter("lastname");
            String address = request.getParameter("address");
            String birthdate = request.getParameter("birthdate");
            java.sql.Date sqlDate = java.sql.Date.valueOf(birthdate);
            String phonenumber = request.getParameter("phonenumber");

            String drivelicensenum = request.getParameter("drlicnumber");
            boolean hasDriverLicense = request.getParameter("driverlcns") != null;


            String creditcardnum = request.getParameter("creditcardnum");
            String creditcardcvv = request.getParameter("creditcardcvv");


            PreparedStatement preparedStatement = null;

            try {
                Connection con = db.getConnection();
                String sql = "INSERT INTO Customer (FirstName,LastName,DateOfBirth,Address,DriversLicenseNumber,PhoneNumber,CardNumber,CVS) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";
                preparedStatement = con.prepareStatement(sql);

                preparedStatement.setString(1, firstname);
                preparedStatement.setString(2, lastname);
                preparedStatement.setDate(3, sqlDate);
                preparedStatement.setString(4, address);
                preparedStatement.setString(6, phonenumber);
                preparedStatement.setString(7, creditcardnum);
                preparedStatement.setString(8, creditcardcvv);

                if (hasDriverLicense && drivelicensenum != null && !drivelicensenum.isEmpty()) {
                    preparedStatement.setString(5, drivelicensenum);
                }
                else {
                    preparedStatement.setNull(5, java.sql.Types.VARCHAR);
                }

                int rowsAffected = preparedStatement.executeUpdate();

                // Print the response to the Java console
                System.out.println("Data uploaded to the database successfully!");
                System.out.println("Rows affected: " + rowsAffected);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }



        }catch( Exception e) {
            e.printStackTrace();
            throw new ServletException("an error occurred processing the request",e);
        }
    }
}