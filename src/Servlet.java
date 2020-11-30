import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;


public class Servlet extends HttpServlet {
    private static final Gson gson = new Gson();
    private static Connection connection;

    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;database=angular", "maga", "qwerty");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Inside");
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");

        try {
            ResultSet rs = connection.createStatement().executeQuery("select * from [dbo].[Books]");

            ArrayList<Book> books = new ArrayList<>();
            int id;
            String name;

            while (rs.next()) {
                id = rs.getInt("id");
                name = rs.getString("name");

                books.add(new Book(id, name));
            }

            PrintWriter pw = resp.getWriter();
            pw.print(gson.toJson(books));

            System.out.println(gson.toJson(books));
            pw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        StringBuilder data = new StringBuilder();
        try (Scanner sc = new Scanner(req.getInputStream())) {
            while (sc.hasNextLine()) {
                data.append(sc.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Book book = gson.fromJson(data.toString(), Book.class);
        String query = "INSERT INTO Books (id, name) VALUES (?, ?)";
        System.out.println(book);
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, book.getId());
            statement.setString(2, book.getName());
            System.out.println(statement.executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
