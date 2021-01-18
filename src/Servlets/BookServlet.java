package Servlets;

import Books.Book;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

public class BookServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        String idParam = req.getParameter("id");

        try (PrintWriter pw = resp.getWriter()) {
            int id;
            String name;
            String authorName;
            int aid;
            if (idParam == null) {
                ResultSet rs = connection.createStatement().executeQuery(
                        "select Books.id, Books.name, (Authors.firstname + ' ' + Authors.lastname) as Author, Books.author_id as aid " +
                                "from [dbo].[Books] LEFT JOIN Authors ON Authors.id = Books.author_id");

                ArrayList<Book> books = new ArrayList<>();
                while (rs.next()) {
                    id = rs.getInt("id");
                    name = rs.getString("name");
                    authorName = rs.getString("Author");
                    aid = rs.getInt("aid");
                    books.add(new Book(id, name, authorName, aid));
                }
                pw.print(gson.toJson(books));
            }else {
                ResultSet rs = connection.createStatement().executeQuery("select Books.id, Books.name, " +
                        "(Authors.firstname + ' ' + Authors.lastname) as Author, Books.author_id as aid " +
                        " from [dbo].[Books] LEFT JOIN Authors ON Authors.id = Books.author_id WHERE Books.id = "
                        + Integer.parseInt(idParam));
                if (rs.next()){
                    id = rs.getInt("id");
                    name = rs.getString("name");
                    authorName = rs.getString("Author");
                    aid = rs.getInt("aid");
                    pw.print(gson.toJson(new Book(id, name,authorName, aid)));
                }
                pw.print(gson.toJson(null));
            }
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


}
