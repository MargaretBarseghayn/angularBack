package Servlets;

import Authors.Author;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

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

                    authors.add(new Author(id, firstName, lastName, birthdate, isAlive));
                }

            } else if(uri.equals("/authors/select")){
                ResultSet rs = connection.createStatement().executeQuery("SELECT id, (firstname+ ' ' + lastname) as name FROM Authors");

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
}
