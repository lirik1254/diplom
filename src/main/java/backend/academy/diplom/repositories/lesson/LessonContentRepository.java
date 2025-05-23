package backend.academy.diplom.repositories.lesson;

import backend.academy.diplom.entities.lesson.LessonContent;
import backend.academy.diplom.repositories.rowmappers.lesson.LessonContentRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LessonContentRepository {
    private final NamedParameterJdbcTemplate template;
    private final LessonContentRowMapper lessonContentRowMapper;

    public LessonContent getLessonContentByLessonId(Long lessonId) {
        String sql = """
                select * from engineers.lesson_content
                where lesson_id = :lessonId""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("lessonId", lessonId);

        return template.query(sql, sqlParameterSource, lessonContentRowMapper).getFirst();
    }
}
