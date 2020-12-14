package Servlets;

import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

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

    protected String read(HttpServletRequest request) {
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
}
