package backend.academy.diplom.repositories.rowmappers.auth;

import backend.academy.diplom.entities.RefreshToken;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

@Component
public class RefreshTokenRowMapper implements RowMapper<RefreshToken> {
    @Override
    public RefreshToken mapRow(ResultSet rs, int rowNum) throws SQLException {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(rs.getLong("id"));
        refreshToken.setUserId(rs.getLong("user_id"));
        refreshToken.setToken(rs.getString("token"));
        Instant instant = rs.getTimestamp("expire_date").toInstant();
        refreshToken.setExpireDate(instant);
        return refreshToken;
    }
}
