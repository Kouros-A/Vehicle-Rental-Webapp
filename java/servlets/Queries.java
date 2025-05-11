package servlets;

import database.DB_Connection;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Queries", value = "/Queries")
public class Queries extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String query = request.getParameter("query");
        String result = executeQuery(query);

        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            out.print(result);
        }
    }
    private String executeQuery(String query) {
        StringBuilder result = new StringBuilder();
        // JDBC URL, username, and password of MySQL server
        DB_Connection db = new DB_Connection();

        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish a connection
            try (            Connection con = db.getConnection();) {
                // Create a statement and execute the user-provided SQL query
                try (PreparedStatement statement = con.prepareStatement(query);
                     ResultSet resultSet = statement.executeQuery()) {
                    // Process the ResultSet and populate the result StringBuilder
                    while (resultSet.next()) {
                        int columnCount = resultSet.getMetaData().getColumnCount();
                        for (int i = 1; i <= columnCount; i++) {
                            result.append(resultSet.getString(i)).append(" ");
                        }
                        result.append("\n");
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            result.append("Error executing query: ").append(e.getMessage());
            e.printStackTrace();
        }

        return result.toString();
    }
}
