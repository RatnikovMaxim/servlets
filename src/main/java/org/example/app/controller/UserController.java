package org.example.app.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.app.dto.UserDTO;
import org.example.app.manager.UserManager;
import org.example.framework.security.Authentication;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor // генерирует конструктор только для final non-static полей
public class UserController {
    private final UserManager manager;
    private final Gson gson;

    @RequestMapping("/users.getAll")
    public void getAll(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        final List<UserDTO> responseDTO = manager.getAll();
        res.getWriter().write(gson.toJson(responseDTO));
    }

    // TODO: http://localhost:8080/users/1
    @GetMapping("/users/{id}")
    public UserDTO getById(@RequestAttribute final Authentication authentication, @PathVariable final long id, final HttpServletResponse res) throws ServletException, IOException {
        // final long id = Long.parseLong(req.getParameter("id")); // req.getParameter - String
        final UserDTO responseDTO = manager.getById(id);
        return responseDTO;
    }

    @RequestMapping("/users.create")
    public void create(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        final String login = req.getParameter("login");
        final String password = req.getParameter("password");
        final UserDTO responseDTO = manager.create(login, password);
        res.getWriter().write(gson.toJson(responseDTO));
    }
}
