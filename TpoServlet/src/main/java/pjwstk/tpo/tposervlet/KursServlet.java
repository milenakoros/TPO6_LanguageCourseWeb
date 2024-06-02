package pjwstk.tpo.tposervlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "KursServlet", urlPatterns = {"/"})
public class KursServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/SzkolaJezykowa";
    private static final String USER = "root";
    private static final String PASS = "haslo123@";

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ServletException("Failed to load MySQL driver", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String language = request.getParameter("language");
        String level = request.getParameter("level");
        String sortOrder = request.getParameter("sortOrder");

        List<Course> courses = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT k.Nazwa_kursu, k.Opis, k.Cena, j.Nazwa_jezyka, p.Nazwa_poziomu " +
                "FROM Kurs k " +
                "JOIN Jezyk j ON k.ID_jezyka = j.ID_jezyka " +
                "JOIN Poziom p ON k.ID_poziomu = p.ID_poziomu " +
                "WHERE 1=1");

        if (language != null && !language.isEmpty()) {
            query.append(" AND j.Nazwa_jezyka = ?");
        }

        if (level != null && !level.isEmpty()) {
            query.append(" AND p.Nazwa_poziomu = ?");
        }

        if (sortOrder != null && sortOrder.equals("desc")) {
            query.append(" ORDER BY k.Cena DESC");
        } else {
            query.append(" ORDER BY k.Cena ASC");
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(query.toString())) {

            int index = 1;
            if (language != null && !language.isEmpty()) {
                pstmt.setString(index++, language);
            }

            if (level != null && !level.isEmpty()) {
                pstmt.setString(index, level);
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Course course = new Course();
                course.setName(rs.getString("Nazwa_kursu"));
                course.setDescription(rs.getString("Opis"));
                course.setPrice(rs.getBigDecimal("Cena"));
                course.setLanguage(rs.getString("Nazwa_jezyka"));
                course.setLevel(rs.getString("Nazwa_poziomu"));
                courses.add(course);
            }
        } catch (Exception e) {
            throw new ServletException("Database error", e);
        }

        List<String> languages = new ArrayList<>();
        List<String> levels = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmtLanguages = conn.prepareStatement("SELECT Nazwa_jezyka FROM Jezyk");
             ResultSet rsLanguages = pstmtLanguages.executeQuery();
             PreparedStatement pstmtLevels = conn.prepareStatement("SELECT Nazwa_poziomu FROM Poziom");
             ResultSet rsLevels = pstmtLevels.executeQuery()) {

            while (rsLanguages.next()) {
                languages.add(rsLanguages.getString("Nazwa_jezyka"));
            }

            while (rsLevels.next()) {
                levels.add(rsLevels.getString("Nazwa_poziomu"));
            }
        } catch (Exception e) {
            throw new ServletException("Database error", e);
        }

        request.setAttribute("courses", courses);
        request.setAttribute("languages", languages);
        request.setAttribute("levels", levels);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
