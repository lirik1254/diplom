package backend.academy.diplom.repositories;

import backend.academy.diplom.entities.Content;
import backend.academy.diplom.repositories.rowmappers.ContentRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ContentRepository {

    private final NamedParameterJdbcTemplate template;
    private final ContentRowMapper contentRowMapper;

    public List<Content> getContentByLessonId(Long lessonId, Long userId) {
        String sql = """
                select * from engineers.content
                where lesson_id = :lessonId
                and :lessonId in (select lesson_id from engineers.lesson_user where user_id = :userId)""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("lessonId", lessonId)
                .addValue("userId", userId);

        return template.query(sql, sqlParameterSource, contentRowMapper);
    }
}
