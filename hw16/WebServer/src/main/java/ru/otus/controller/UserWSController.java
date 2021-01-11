package ru.otus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.scheduling.annotation.Scheduled;
import ru.otus.db.core.model.User;
import ru.otus.front.FrontendService;

@Controller
public class UserWSController {

    private static final Logger logger = LoggerFactory.getLogger(UserWSController.class);
    private final FrontendService frontendService;
    private final SimpMessagingTemplate template;

    public UserWSController(FrontendService frontendService, SimpMessagingTemplate template) {
        this.frontendService = frontendService;
        this.template = template;
    }

    @MessageMapping("/user")
    public void insertUser(User user) {
        frontendService.insertUser(user, data -> {
            logger.info("user with ID{} was inserted", user.getId());
        });
    }

    @MessageMapping("/users")
    public void updateUsers() {
        logger.info("call");
        frontendService.getUsers(data -> {
            template.convertAndSend("/topic/users", data.getUsers());
        });
    }

}
