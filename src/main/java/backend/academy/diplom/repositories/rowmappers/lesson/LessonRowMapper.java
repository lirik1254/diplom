package backend.academy.diplom.repositories.rowmappers.lesson;

import backend.academy.diplom.entities.lesson.Lesson;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LessonRowMapper implements RowMapper<Lesson> {
    @Override
    public Lesson mapRow(ResultSet rs, int rowNum) throws SQLException {
        Lesson lesson = new Lesson();
        lesson.setId(rs.getLong("id"));
        lesson.setName(rs.getString("name"));
        lesson.setModuleId(rs.getLong("module_id"));
        lesson.setAuthorId(rs.getLong("author_id"));
        lesson.setNumber(rs.getInt("number"));

        return lesson;
    }
}
