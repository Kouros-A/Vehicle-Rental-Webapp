package servlets;

import database.DB_Connection;

import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet(name = "Motorbikes", value = "/Motorbikes")
public class Motorbikes extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String[]> dataList = new ArrayList<>();
        DB_Connection db = new DB_Connection();
        PreparedStatement preparedStatement = null;
        try {
            Connection con = db.getConnection();
            String sql = "SELECT * FROM CarsMotorbikes WHERE Type='Motorbike' AND Availability=1";
            try (PreparedStatement statement = con.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                // Process the ResultSet and populate the dataList
                while (resultSet.next()) {
                    String[] row = new String[10];
                    for (int i = 1; i <= 10; i++) {
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

    }


    // Convert List<String[]> to JSON
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
