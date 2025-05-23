package backend.academy.diplom.repositories.rowmappers;

import backend.academy.diplom.entities.user.UserEducation;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EducationRowMapper implements RowMapper<UserEducation> {
    @Override
    public UserEducation mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserEducation userEducation = new UserEducation();

        userEducation.setId(rs.getLong("id"));
        userEducation.setUserId(rs.getLong("user_id"));
        userEducation.setEducation(rs.getString("education"));

        return userEducation;
    }
}
