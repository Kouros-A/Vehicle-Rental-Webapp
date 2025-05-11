package servlets;

import database.DB_Connection;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@WebServlet(name = "ScooterBike", value = "/ScooterBike")
public class ScooterBike extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        List<String[]> dataList = new ArrayList<>();
        DB_Connection db = new DB_Connection();
        PreparedStatement preparedStatement = null;
        try {
            Connection con = db.getConnection();
            String sql = "SELECT * FROM scootersbikes WHERE Availability=1";
            try (PreparedStatement statement = con.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                // Process the ResultSet and populate the dataList
                while (resultSet.next()) {
                    String[] row = new String[8];
                    for (int i = 1; i <= 8; i++) {
                        row[i - 1] = resultSet.getString(i);
                    }
                    dataList.add(row);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        // Send the data as JSON response
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            out.print(toJson(dataList));
        }
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
            String uniqueNumber = request.getParameter("uniqueNumber");

            // Convert parameters to appropriate types
            int autonomy = Integer.parseInt(autonomyParam);
            double rentalCost = Double.parseDouble(rentalCostParam);
            double insuranceCost = Double.parseDouble(insuranceCostParam);

            PreparedStatement preparedStatement = null;

            try {
                Connection con = db.getConnection();
                // Prepare the SQL statement
                String sql = "INSERT INTO ScootersBikes (Brand, Model, Color, Autonomy, RentalCost, InsuranceCost, UniqueNumber) VALUES (?, ?, ?, ?, ?, ?, ?)";
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, brand);
                preparedStatement.setString(2, model);
                preparedStatement.setString(3, color);
                preparedStatement.setInt(4, autonomy);
                preparedStatement.setDouble(5, rentalCost);
                preparedStatement.setDouble(6, insuranceCost);
                preparedStatement.setString(7, uniqueNumber);
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
    private String toJson(List<String[]> dataList) {
        StringBuilder json = new StringBuilder("[");
        for (String[] row : dataList) {
            json.append("[");
            for (int i = 0; i < row.length; i++) {
                json.append("\"").append(row[i]).append("\"");
                if (i < row.length - 1) {
                    json.append(",");
                }
            }
            json.append("],");
        }
        if (json.charAt(json.length() - 1) == ',') {
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }
}
