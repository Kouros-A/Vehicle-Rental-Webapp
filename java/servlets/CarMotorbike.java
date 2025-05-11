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


@WebServlet(name = "CarMotorbike", value = "/CarMotorbike")
public class CarMotorbike extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            DB_Connection db = new DB_Connection();

            // Retrieve form data
            String brand = request.getParameter("brand");
            String model = request.getParameter("model");
            String color = request.getParameter("color");
            String autonomyParam = request.getParameter("autonomy");
            String rentalCostParam = request.getParameter("rentalCost");
            String insuranceCostParam = request.getParameter("insuranceCost");
            String type = request.getParameter("type");
            String passengersNumberParam = request.getParameter("passengersNumber");
            String registerNumber = request.getParameter("registerNumber");

            // Convert parameters to appropriate types
            int autonomy = Integer.parseInt(autonomyParam);
            double rentalCost = Double.parseDouble(rentalCostParam);
            double insuranceCost = Double.parseDouble(insuranceCostParam);
            Integer passengersNumber = (passengersNumberParam != null && !passengersNumberParam.isEmpty())
                    ? Integer.parseInt(passengersNumberParam) : null;

            PreparedStatement preparedStatement = null;

            try {
                Connection con = db.getConnection();
                // Prepare the SQL statement
                String sql = "INSERT INTO CarsMotorbikes (Brand, Model, Color, Autonomy, RentalCost, InsuranceCost, Type, PassengersNumber, RegisterNumber) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, brand);
                preparedStatement.setString(2, model);
                preparedStatement.setString(3, color);
                preparedStatement.setInt(4, autonomy);
                preparedStatement.setDouble(5, rentalCost);
                preparedStatement.setDouble(6, insuranceCost);
                preparedStatement.setString(7, type);
                preparedStatement.setObject(8, passengersNumber); // Handles null for PassengersNumber
                preparedStatement.setString(9, registerNumber);
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
