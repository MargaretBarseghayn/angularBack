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
        String[] reqSplit = req.getRequestURI().split("/");
        String query = reqSplit[reqSplit.length - 1];
        ArrayList<Author> authors = new ArrayList<>();

        try (PrintWriter pw = resp.getWriter()) {
            System.out.println("Query" + query);
            switch (query) {
                case "list": {
                    ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Authors");
                    int id;
                    String firstName, lastName;
                    Date birthdate, deathdate;
                    boolean isAlive;
                    while (rs.next()) {
                        id = rs.getInt("id");
                        firstName = rs.getString("firstname");
                        lastName = rs.getString("lastname");
                        birthdate = rs.getDate("birthdate");
                        deathdate = rs.getDate("deathdate");
                        // isAlive = rs.getBoolean("isAlive");
                        authors.add(new Author(id, firstName, lastName, birthdate != null ? (birthdate.getTime()) : 1, deathdate != null ? deathdate.getTime() : null));
                        authors.forEach(author -> System.out.println(author.toString()));
                    }
                    pw.print(gson.toJson(authors));
                    break;
                }
                case "select": {
                    ResultSet rs = connection.createStatement().executeQuery("SELECT id, (firstname + ' ' + lastname) as name FROM Authors");
                    int id;
                    String name;
                    while (rs.next()) {
                        id = rs.getInt("id");
                        name = rs.getString("name");
                        authors.add(new Author(id, name));
                    }
                    pw.print(gson.toJson(authors));
                    break;
                }
                case "id": {
                    int idParam = Integer.parseInt(req.getParameter("id"));
                    ResultSet rs = connection.createStatement().executeQuery("SELECT ID, FIRSTNAME, LASTNAME, " +
                            "BIRTHDATE, DEATHDATE  FROM AUTHORS WHERE ID = " + idParam);

                    int id;
                    String firstName, lastName;
                    Date birthDate, deathDate;
                    if (rs.next()) {
                        id = rs.getInt("id");
                        firstName = rs.getString("firstname");
                        lastName = rs.getString("lastname");
                        birthDate = rs.getDate("birthdate");
                        deathDate = rs.getDate("deathdate");
                        pw.print(gson.toJson(new Author(id, firstName, lastName, birthDate != null ? (birthDate.getTime()) : 1, deathDate != null ? deathDate.getTime() : null)));
                    } else pw.print(gson.toJson(null));
                    break;
                }
            }

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");

        String data = read(req);
        Author author = gson.fromJson(data, Author.class);
        String query = "INSERT INTO Authors(firstname, lastname, birthdate, deathdate) output Inserted.id VALUES(?, ?, ?, ?);";
        String id = null;
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, author.getFirstName());
            ps.setString(2, author.getLastName());
            ps.setDate(3, author.getBirthdate());
            ps.setDate(4, author.getDeathdate());
//            ps.setBoolean(4, author.isAlive());

            ResultSet rs = ps.executeQuery();
            rs.next();
            id = gson.toJson(rs.getInt("id"));

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
        System.out.println("data" + data);
        Author author = gson.fromJson(data, Author.class);
        System.out.println("Author" + author);

        int editedCount = 0;
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE Authors SET firstname = ?, lastname = ?, birthdate = ?, deathdate = ? WHERE id = ?");
            ps.setString(1, author.getFirstName());
            ps.setString(2, author.getLastName());
            ps.setDate(3, author.getBirthdate());
            ps.setDate(4, author.getDeathdate());
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


