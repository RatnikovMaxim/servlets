package org.example.framework.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.framework.security.Authentication;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@WebFilter("/*")
// Filter
@Slf4j
public class AuthFilter extends HttpFilter {
    // TODO: replace with db
    private final Map<String, String> users = Map.of(
      "vasya", "secret",
      "petya", "secret"
    );

    // log - logger
    @Override
    protected void doFilter(final HttpServletRequest req, final HttpServletResponse res, final FilterChain chain) throws IOException, ServletException {
        // before servlet
        // ctrl + alt + t -> surround with
        // TODO:
        //  servlets -> 1. Old School -> req.getHeader("Login") -> null
        //  2. Modern -> req.getHeader("Login") -> Optional
        //  3. Strict -> req.getHeader("Login") -> Exception/RuntimeException
        final String login = req.getHeader("X-Login");
        final String password = req.getHeader("X-Password");
        // TODO: credential
        //  -> log.trace
        //  -> log.debug
        log.debug("login: {}, password: {}", login, password);
        // TODO:
        //  1. DB (map)
        //  2. Hash & Compare
        //  3. User

        // TODO:
        //  -> 1. Exception/Error Status Code
        //  2. Anonymous

        // ifn + Tab
        if (login == null) {
            res.setStatus(401);
            res.getWriter().write("Not authenticated");
            return; // чтобы не попало в chain.doFilter
        }

        if (password == null) {
            res.setStatus(401);
            res.getWriter().write("Not authenticated");
            return; // чтобы не попало в chain.doFilter
        }

        // NPE - NullPointerException
        if (!Objects.equals(users.get(login), password)) {
            res.setStatus(401);
            res.getWriter().write("Not authenticated");
            return; // чтобы не попало в chain.doFilter
        }

        // TODO: request достаточно часто используют для передачи через атрибуты доп.значений (например, аутентификации)
        final Authentication authentication = new Authentication(login);
        req.setAttribute("authentication", authentication);

        chain.doFilter(req, res);
    }
}
