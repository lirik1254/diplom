package backend.academy.diplom.repositories.auth;

import backend.academy.diplom.entities.RefreshToken;
import backend.academy.diplom.repositories.rowmappers.auth.RefreshTokenRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenRepository {
    private final NamedParameterJdbcTemplate template;
    private final RefreshTokenRowMapper refreshTokenRowMapper;

    public void deleteByUserId(long userId) {
        String deleteQuery = """
                delete from engineers.refresh_token
                where user_id = :userId""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("userId", userId);

        try {
            template.query(deleteQuery, sqlParameterSource, refreshTokenRowMapper);
        } catch (Exception e) {
            log.info("Удаление не удалось");
        }
    }

    public RefreshToken save(RefreshToken refreshToken) {
        String insertQuery = """
                insert into engineers.refresh_token(expire_date, token, user_id)
                values (:expireDate, :token, :userId)""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("userId", refreshToken.getUserId())
                .addValue("token", refreshToken.getToken())
                .addValue("expireDate", Timestamp.from(refreshToken.getExpireDate()));

        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(insertQuery, sqlParameterSource, keyHolder, new String[]{"id"});

        refreshToken.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return refreshToken;
    }

    public void delete(RefreshToken refreshToken) {
        String deleteSql = """
                delete from engineers.refresh_token
                where id = :id and user_id = :userId
                and token = :token and expire_date = :expireDate""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("id", refreshToken.getId())
                .addValue("userId", refreshToken.getUserId())
                .addValue("token", refreshToken.getToken())
                .addValue("expireDate", Timestamp.from(refreshToken.getExpireDate()));

        template.update(deleteSql, sqlParameterSource);
    }

    public List<RefreshToken> findRefreshTokenByToken(String token) {
        String findSql = """
                select * from engineers.refresh_token
                where token = :token""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("token", token);

        return template.query(findSql, sqlParameterSource, refreshTokenRowMapper);
    }
}
