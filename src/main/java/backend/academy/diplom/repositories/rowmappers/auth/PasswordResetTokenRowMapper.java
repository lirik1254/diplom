package backend.academy.diplom.repositories.rowmappers.auth;

import backend.academy.diplom.entities.PasswordResetToken;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

@Component
public class PasswordResetTokenRowMapper implements RowMapper<PasswordResetToken> {
    @Override
    public PasswordResetToken mapRow(ResultSet rs, int rowNum) throws SQLException {
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setId(rs.getLong("id"));
        resetToken.setUserId(rs.getLong("user_id"));
        resetToken.setToken(rs.getString("token"));
        Instant instant = rs.getTimestamp("expire_date").toInstant();
        resetToken.setExpireDate(instant);
        return resetToken;
    }
}
