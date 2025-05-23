package backend.academy.diplom.repositories.rowmappers.auth;

import backend.academy.diplom.entities.user.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setSurname(rs.getString("surname"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setBirthDate(rs.getDate("birth_date"));
        user.setCity(rs.getString("city"));
        user.setStatus(rs.getString("status"));
        user.setDiplomaPath(rs.getString("diploma_path"));
        user.setResumePath(rs.getString("resume_path"));
        user.setPhotoPath(rs.getString("photo_path"));
        user.setAbout(rs.getString("about"));
        user.setTelegram(rs.getString("telegram"));
        user.setIsPublic(rs.getBoolean("is_public"));
        user.setHideBirthday(rs.getBoolean("hide_birthday"));
        user.setGender(rs.getString("gender"));
        return user;
    }
}
