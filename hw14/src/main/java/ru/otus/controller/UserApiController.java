package ru.otus.controller;

import org.springframework.web.bind.annotation.*;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class UserApiController {

    private final DBServiceUser serviceUser;

    public UserApiController(DBServiceUser serviceUser) {
        this.serviceUser = serviceUser;
    }

    @GetMapping("/api/user/")
    public List<User> getUsers() {
        return serviceUser.getAllUsers();
    }

    @PostMapping(path = "/api/user/")
    public void postUser(@RequestBody User user, HttpServletResponse resp) {
        long l = serviceUser.saveUser(user);
        if (l > 0) {
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
    }

}
