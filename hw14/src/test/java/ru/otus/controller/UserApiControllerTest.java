package ru.otus.controller;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserApiControllerTest {

    private MockMvc mvc;
    private Gson gson;

    @Mock
    private DBServiceUser userService;

    @BeforeEach
    public void init() {
        gson = new Gson();
        mvc = MockMvcBuilders.standaloneSetup(new UserApiController(userService)).build();
    }

    @Test
    void insertUser() throws Exception {
        User cat = new User(0L, "CAT", 4, null, null);
        String newUserJson = gson.toJson(cat);

        given(userService.saveUser(any(User.class))).willReturn(1L);

        mvc.perform(post("/api/user/")
                .contentType("application/json")
                .content(newUserJson))
                .andExpect(status().isCreated());
    }

    @Test
    void getAllUsers() throws Exception {

        User cat1 = new User(0L, "CAT", 4, null, null);
        User cat2 = new User(0L, "CAT", 4, null, null);

        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(cat1);
        expectedUsers.add(cat2);


        given(userService.getAllUsers()).willReturn(expectedUsers);

        mvc.perform(get("/api/user/"))
                .andExpect(status().isOk())
                .andExpect(content().string(gson.toJson(expectedUsers)));
    }
}
