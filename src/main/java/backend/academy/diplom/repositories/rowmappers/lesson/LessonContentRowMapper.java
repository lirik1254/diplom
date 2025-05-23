package backend.academy.diplom.repositories.rowmappers.lesson;

import backend.academy.diplom.entities.lesson.LessonContent;
import backend.academy.diplom.repositories.lesson.LessonContentRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LessonContentRowMapper implements RowMapper<LessonContent> {
    @Override
    public LessonContent mapRow(ResultSet rs, int rowNum) throws SQLException {
        LessonContent lessonContent = new LessonContent();

        lessonContent.setId(rs.getLong("id"));
        lessonContent.setLessonText(rs.getString("lesson_text"));
        lessonContent.setVideoUrl(rs.getString("video_url"));
        lessonContent.setLessonTaskText(rs.getString("lesson_task_text"));
        lessonContent.setLessonId(rs.getLong("lesson_id"));

        return lessonContent;
    }
}
