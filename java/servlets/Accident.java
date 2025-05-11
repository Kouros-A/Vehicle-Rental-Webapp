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


@WebServlet(name = "Accident", value = "/Accident")
public class Accident extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            DB_Connection db = new DB_Connection();

            // Retrieve form data
            String rentalId = request.getParameter("rentalId");
            String description = request.getParameter("description");
            String resolutionStatus = request.getParameter("resolutionStatus");

            PreparedStatement preparedStatement = null;

            try {
                Connection con = db.getConnection();
                String sql = "INSERT INTO Accident (RentalId, Description, ResolutionStatus) VALUES (?, ?, ?)";
                preparedStatement = con.prepareStatement(sql);

                preparedStatement.setInt(1, Integer.parseInt(rentalId));
                preparedStatement.setString(2, description);
                preparedStatement.setString(3, resolutionStatus);
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
