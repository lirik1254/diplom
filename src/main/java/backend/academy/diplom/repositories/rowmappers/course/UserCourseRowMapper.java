package backend.academy.diplom.repositories.rowmappers.course;

import backend.academy.diplom.entities.user.UserCourse;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserCourseRowMapper implements RowMapper<UserCourse> {

    @Override
    public UserCourse mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserCourse userCourse = new UserCourse();

        userCourse.setUserId(rs.getLong(("user_id")));
        userCourse.setCourseId(rs.getLong("course_id"));
        userCourse.setLastLessonId(rs.getLong("last_lesson_id"));

        return userCourse;
    }
}
