package backend.academy.diplom.repositories.courses;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TagRepository {
    private final NamedParameterJdbcTemplate template;

    public List<String> getAllCourseTags(Long courseId) {
        String sql = """
            SELECT t.name FROM engineers.tag t
            JOIN engineers.course_tag ct ON t.id = ct.tag_id
            WHERE ct.course_id = :courseId""";

        return template.query(sql,
                new MapSqlParameterSource("courseId", courseId),
                (rs, rowNum) -> rs.getString("name"));
    }
}
