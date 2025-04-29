package backend.academy.diplom.repositories.rowmappers.lesson;

import backend.academy.diplom.entities.LessonUser;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LessonUserRowMapper implements RowMapper<LessonUser> {
    @Override
    public LessonUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        LessonUser lessonUser = new LessonUser();
        lessonUser.setLessonId(rs.getLong("lesson_id"));
        lessonUser.setUserId(rs.getLong("user_id"));
        lessonUser.setHere(rs.getBoolean("is_here"));

        return lessonUser;
    }
}
