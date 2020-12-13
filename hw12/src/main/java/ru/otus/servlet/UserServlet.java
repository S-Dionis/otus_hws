package ru.otus.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class UserServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(UserServlet.class);
    private static final String PATH = "templates/user.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        URL resource = UserServlet.class.getResource("/templates/user.html");
        try {
            Path path = Path.of(resource.toURI());
            String file = Files.readString(path);
            resp.getWriter().println(file);
        } catch (URISyntaxException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
