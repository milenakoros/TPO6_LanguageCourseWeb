<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="pjwstk.tpo.tposervlet.Course" %>
<%@ page import="java.util.List" %>
<html>
<head>
  <title>Kursy Szkoły Językowej</title>
  <link rel="stylesheet" type="text/css" href="styles/styles.css">
</head>
<body>
<div class="header">Kursy Szkoły Językowej SpeakEasy </div>
<div class="search-box">
  <form action="" method="get">
    <select name="language">
      <option value="">Wszystkie Języki</option>
      <%
        List<String> languages = (List<String>) request.getAttribute("languages");
        if (languages != null && !languages.isEmpty()) {
          for (String lang : languages) {
      %>
      <option value="<%= lang %>"><%= lang %></option>
      <%
        }
      } else {
      %>
      <option value="">Brak dostępnych języków</option>
      <%
        }
      %>
    </select>
    <select name="level">
      <option value="">Wszystkie Poziomy</option>
      <%
        List<String> levels = (List<String>) request.getAttribute("levels");
        if (levels != null && !levels.isEmpty()) {
          for (String lvl : levels) {
      %>
      <option value="<%= lvl %>"><%= lvl %></option>
      <%
        }
      } else {
      %>
      <option value="">Brak dostępnych poziomów</option>
      <%
        }
      %>
    </select>
    <select name="sortOrder">
      <option value="asc">Cena Rosnąco</option>
      <option value="desc">Cena Malejąco</option>
    </select>
    <input type="submit" value="Filtruj">
  </form>
</div>
<div class="table-container">
  <table>
    <thead>
    <tr>
      <th>Nazwa Kursu</th>
      <th>Opis</th>
      <th>Poziom</th>
      <th>Cena za msc</th>
      <th>Język</th>
    </tr>
    </thead>
    <tbody>
    <%
      List<Course> courses = (List<Course>) request.getAttribute("courses");
      if (courses != null && !courses.isEmpty()) {
        for (Course course : courses) {
    %>
    <tr>
      <td><%= course.getName() %></td>
      <td><%= course.getDescription() %></td>
      <td><%= course.getLevel() %></td>
      <td><%= course.getPrice() %></td>
      <td><%= course.getLanguage() %></td>
    </tr>
    <%
      }
    } else {
    %>
    <tr>
      <td colspan="5">Brak dostępnych kursów</td>
    </tr>
    <%
      }
    %>
    </tbody>
  </table>
</div>
</body>
</html>
