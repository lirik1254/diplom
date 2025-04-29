package backend.academy.diplom.repositories.auth;

import backend.academy.diplom.entities.PasswordResetToken;
import backend.academy.diplom.repositories.rowmappers.auth.PasswordResetTokenRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PasswordResetTokenRepository {
    private final NamedParameterJdbcTemplate template;
    private final PasswordResetTokenRowMapper passwordResetTokenRowMapper;

    public void saveToken(PasswordResetToken passwordResetToken) {
        String insertQuery = """
                insert into engineers.password_reset_token(expire_date, token, user_id)
                values (:expireDate, :token, :userId)""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("userId", passwordResetToken.getUserId())
                .addValue("token", passwordResetToken.getToken())
                .addValue("expireDate", Timestamp.from(passwordResetToken.getExpireDate()));

        template.update(insertQuery, sqlParameterSource);
    }

    public List<PasswordResetToken> getTokenByToken(String token) {
        String getTokenQuery = """
                select * from engineers.password_reset_token
                where token = :token""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("token", token);

        try {
            return template.query(getTokenQuery, sqlParameterSource, passwordResetTokenRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return List.of();
        }
    }

    public void deleteByToken(PasswordResetToken passwordResetToken) {
        String deleteQuery = """
                delete from engineers.password_reset_token
                where token = :token""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("token",
                passwordResetToken.getToken());

        template.update(deleteQuery, sqlParameterSource);
    }

    public void deleteTokenByEmail(String email) {
        String deleteQuery = """
                delete from engineers.password_reset_token
                where user_id = (select id from engineers.user
                where email = :email)""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("email", email);

        try {
            template.update(deleteQuery, sqlParameterSource);
        } catch (Exception e) {
            log.info("токен не был удалён");
        }
    }

}
