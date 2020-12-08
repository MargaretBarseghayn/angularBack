package Servlets;

import Books.Book;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;


public class BookServlet extends BaseServlet {

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
            ResultSet rs = connection.createStatement().executeQuery(
                    "select Books.id, Books.name, (Authors.firstname + ' ' + Authors.lastname) as Author from [dbo].[Books] LEFT JOIN Authors ON Authors.id = Books.author_id");

            ArrayList<Book> books = new ArrayList<>();
            int id;
            String name;
            String authorName;

            while (rs.next()) {
                id = rs.getInt("id");
                name = rs.getString("name");
                authorName = rs.getString("Author");

                books.add(new Book(id, name, authorName, null));
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
        Book book = gson.fromJson(data, Book.class);
        String query = "INSERT INTO Books([name], author_id) output Inserted.id values(?, ?);";
        String id = null;

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, book.getName());
            statement.setInt(2, book.getAuthorId());

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
            PreparedStatement ps = connection.prepareStatement("UPDATE Books SET name = ?, author_id = ? WHERE id = ?");
            ps.setString(1, book.getName());
            ps.setInt(2, book.getAuthorId());
            ps.setInt(3, book.getId());
            editedCount = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        resp.getWriter().print(editedCount >= 1);

    }
}