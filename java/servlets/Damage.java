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


@WebServlet(name = "Damage", value = "/Damage")
public class Damage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            DB_Connection db = new DB_Connection();
            // Retrieve form data
            String carMotorbikeId = request.getParameter("carMotorbikeId");
            String scooterBikeId = request.getParameter("scooterBikeId");
            String description = request.getParameter("description");
            String repairStatus = request.getParameter("repairStatus");
            String cost = request.getParameter("cost");
            PreparedStatement preparedStatement = null;

            try {
                Connection con = db.getConnection();
                String sql = "INSERT INTO damage (CarMotorbikeId, ScooterBikeId, Description, RepairStatus, Cost) VALUES (?, ?, ?, ?, ?)";
                preparedStatement = con.prepareStatement(sql);
                if (carMotorbikeId != null) {
                    preparedStatement.setInt(1, Integer.parseInt(carMotorbikeId));
                    preparedStatement.setNull(2, Types.INTEGER);

                } else if (scooterBikeId != null) {
                    preparedStatement.setNull(1, Types.INTEGER);
                    preparedStatement.setInt(2, Integer.parseInt(scooterBikeId));
                }


                preparedStatement.setString(3, description);
                preparedStatement.setBoolean(4, Integer.parseInt(repairStatus) == 1);
                preparedStatement.setDouble(5, Double.parseDouble(cost));

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
