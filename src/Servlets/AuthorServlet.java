package Servlets;

import Authors.Author;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AuthorServlet extends BaseServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        String uri = req.getRequestURI();
        ArrayList<Author> authors = new ArrayList<>();

        try (PrintWriter pw = resp.getWriter()) {
            if (uri.equals("/authors/list")) {
                ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Authors");

                int id;
                String firstName, lastName;
                Date birthdate;
                boolean isAlive;
                while (rs.next()) {
                    id = rs.getInt("id");
                    firstName = rs.getString("firstname");
                    lastName = rs.getString("lastname");
                    birthdate = rs.getDate("birthdate");
                    isAlive = rs.getBoolean("isAlive");

                    authors.add(new Author(id, firstName, lastName, birthdate != null ? (birthdate.getTime())  : 1, isAlive));
                }

            } else if (uri.equals("/authors/select")) {
                ResultSet rs = connection.createStatement().executeQuery("SELECT id, (firstname + ' ' + lastname) as name FROM Authors");

                int id;
                String name;
                while (rs.next()) {
                    id = rs.getInt("id");
                    name = rs.getString("name");

                    authors.add(new Author(id, name));
                }
            }

            pw.print(gson.toJson(authors));

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");

        String data = read(req);
        System.out.println("data" + data);
        Author author = gson.fromJson(data, Author.class);
        String query = "INSERT INTO Authors(firstname, lastname, birthdate, isAlive) output Inserted.id VALUES(?, ?, ?, ?);";
        String id = null;
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, author.getFirstName());
            ps.setString(2, author.getLastName());
            ps.setDate(3, author.getBirthdate());
            ps.setBoolean(4, author.isAlive());

            ResultSet rs = ps.executeQuery();
            rs.next();
            id = gson.toJson(rs.getInt("id"));
            System.out.println("id"+ id);

        } catch (SQLException throwable) {
            throwable.printStackTrace();
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
        Author author = gson.fromJson(data, Author.class);

        int editedCount = 0;
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE Authors SET firstname = ?, lastname = ?, birthdate = ?, isAlive = ? WHERE id = ?");
            ps.setString(1, author.getFirstName());
            ps.setString(2, author.getLastName());
            ps.setDate(3, author.getBirthdate());
            ps.setBoolean(4, author.isAlive());
            ps.setInt(5, author.getId());

            editedCount = ps.executeUpdate();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        resp.getWriter().print(editedCount >= 1);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");

        int editedCount = 0;
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Books WHERE author_id = ?");
            ps.setString(1, req.getParameter("id"));
            ps.executeUpdate();
            ps = connection.prepareStatement("DELETE FROM Authors WHERE id = ?");
            ps.setString(1, req.getParameter("id"));
            editedCount = ps.executeUpdate();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        resp.getWriter().print(editedCount >= 1);
    }
}


