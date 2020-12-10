package ru.otus.servlet;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserApiServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(UserApiServlet.class);

    private final Gson gson;
    private final DBServiceUser serviceUser;

    public UserApiServlet(DBServiceUser serviceUser, Gson gson) {
        this.serviceUser = serviceUser;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        List<User> allUsers = serviceUser.getAllUsers();
        logger.info("users found:" + allUsers.size());
        logger.info("last user inserted:" + allUsers.get(allUsers.size() - 1));
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(gson.toJson(allUsers.toArray()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = gson.fromJson(req.getReader(), User.class);
        long l = serviceUser.saveUser(user);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
