package org.example.app.manager;

import lombok.RequiredArgsConstructor;
import org.example.app.dto.UserDTO;
import org.example.app.exception.UserLoginAlreadyRegisteredException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserManager {
    private final NamedParameterJdbcOperations jdbcOperations;
    private final RowMapper<UserDTO> rowMapper = (rs, rowNum) ->
            new UserDTO(rs.getLong("id"), rs.getString("login"));

    public List<UserDTO> getAll() {
        return jdbcOperations.query(
                // language=PostgreSQL
                """
                SELECT id, login FROM users
                """,
                rowMapper
        );
    }

    public UserDTO getById(final long id) {
        return jdbcOperations.queryForObject(
                // language=PostgreSQL
                """
                SELECT id, login FROM users WHERE id = :id
                """,
                Map.of("id", id),
                rowMapper
        );
    }

    public UserDTO create(final String login, final String password) {
        Objects.requireNonNull(login);

        final boolean loginAlreadyRegistered = jdbcOperations.queryForObject(
                // language=PostgreSQL
                """
                SELECT count(*) != 0 FROM users WHERE login = :login
                """,
                Map.of("login", login),
                Boolean.class
        );
        if (loginAlreadyRegistered) {
            throw new UserLoginAlreadyRegisteredException(login);
        }

        return jdbcOperations.queryForObject(
                // language=PostgreSQL
                """
                INSERT INTO users(login, password) VALUES (:login, :password) RETURNING id, login
                """,
                Map.of(
                        "login", login,
                        "password", password
                ),
                rowMapper
        );
    }
}
