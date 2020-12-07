package Servlets;

import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.DriverManager;

public class BaseServlet extends HttpServlet {
    protected static final Gson gson = new Gson();
    protected static Connection connection;

    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;database=angular", "maga", "qwerty");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
