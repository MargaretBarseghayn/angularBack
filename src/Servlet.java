import com.google.gson.Gson;

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

    private String read(HttpServletRequest request) {
        StringBuilder data = new StringBuilder();
        try (Scanner sc = new Scanner(request.getInputStream())) {
            while (sc.hasNextLine()) {
                data.append(sc.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.toString();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");

        try (PrintWriter pw = resp.getWriter()) {
            ResultSet rs = connection.createStatement().executeQuery("select * from [dbo].[Books]");

            ArrayList<Book> books = new ArrayList<>();
            int id;
            String name;

            while (rs.next()) {
                id = rs.getInt("id");
                name = rs.getString("name");

                books.add(new Book(id, name));
            }
            pw.print(gson.toJson(books));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        String data = read(req);
        BookData book = gson.fromJson(data, BookData.class);
        String query = "INSERT INTO Books([name]) output Inserted.id values(?);";
        String id = null;

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, book.getName());

            ResultSet rs = statement.executeQuery();
            rs.next();
            id = gson.toJson(rs.getInt("id"));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        resp.getWriter().print(id == null ? false : id);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        int editedCount = 0;

        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Books where id = ?");
            ps.setString(1, req.getParameter("id"));
            editedCount = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        resp.getWriter().print(editedCount >= 1);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        String data = read(req);
        Book book = gson.fromJson(data, Book.class);
        int editedCount = 0;
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE Books SET name = ? WHERE id = ?");
            ps.setString(1, book.getName());
            ps.setInt(2, book.getId());
            editedCount = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        resp.getWriter().print(editedCount >= 1);

    }
}
