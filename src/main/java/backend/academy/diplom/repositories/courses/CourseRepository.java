package backend.academy.diplom.repositories.courses;

import backend.academy.diplom.entities.Course;
import backend.academy.diplom.repositories.rowmappers.CourseModuleNameRowMapper;
import backend.academy.diplom.repositories.rowmappers.IdRowMapper;
import backend.academy.diplom.repositories.rowmappers.course.CourseRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CourseRepository {
    private final CourseRowMapper courseRowMapper;
    private final NamedParameterJdbcTemplate template;
    private final CourseModuleNameRowMapper courseModuleNameRowMapper;
    private final IdRowMapper idRowMapper;

    public List<Course> getAllCoursesByEmail(String email) {
        String getCourseQuery = """
                select * from engineers.course c
                join engineers.user_course uc on c.id = uc.course_id
                join engineers.user u on u.id = uc.user_id
                where email = :email""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("email", email);

        return template.query(getCourseQuery, sqlParameterSource, courseRowMapper);
    }

    public String getCourseModuleName(Long lessonId) {
        String sql = """
                select 'Урок '  || m.number::text || '.' || l.number::text as name from engineers.module m
                join engineers.lesson l on m.id = l.module_id
                where l.id = :lessonId""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("lessonId", lessonId);

        return template.query(sql, sqlParameterSource, courseModuleNameRowMapper).getFirst();
    }

    public Long getBeforeLesson(Long lessonId) {
        String sql = """
                SELECT l.id
                FROM engineers.lesson l
                JOIN engineers.module m ON l.module_id = m.id
                JOIN engineers.course_module mc ON m.id = mc.module_id
                WHERE mc.course_id = (
                    SELECT mc.course_id
                    FROM engineers.lesson l
                    JOIN engineers.module m ON l.module_id = m.id
                    JOIN engineers.course_module mc ON m.id = mc.module_id
                    WHERE l.id = :lessonId
                )
                AND l.id < :lessonId
                ORDER BY l.id DESC
                LIMIT 1""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("lessonId", lessonId);

        List<Long> result = template.query(sql, sqlParameterSource, idRowMapper);
        return result.isEmpty() ? null : result.getFirst();
    }

    public Long getAfterLesson(Long lessonId) {
        String sql = """
                SELECT l.id
                FROM engineers.lesson l
                JOIN engineers.module m ON l.module_id = m.id
                JOIN engineers.course_module mc ON m.id = mc.module_id
                WHERE mc.course_id = (
                    SELECT mc.course_id
                    FROM engineers.lesson l
                    JOIN engineers.module m ON l.module_id = m.id
                    JOIN engineers.course_module mc ON m.id = mc.module_id
                    WHERE l.id = :lessonId
                )
                AND l.id > :lessonId
                ORDER BY l.id ASC
                LIMIT 1;""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("lessonId", lessonId);

        List<Long> result = template.query(sql, sqlParameterSource, idRowMapper);
        return result.isEmpty() ? null : result.getFirst();
    }

    public List<Course> getMainPreview() {
        String sql = """
                select * from engineers.course
                limit 3""";

        return template.query(sql, courseRowMapper);
    }

    public List<Course> getAllPreview() {
        String sql = """
                select * from engineers.course
                """;

        return template.query(sql, courseRowMapper);
    }

    public Course getCourseById(Long courseId) {
        String sql = """
                select * from engineers.course
                where id = :courseId""";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("courseId", courseId);
        return template.query(sql, sqlParameterSource, courseRowMapper).getFirst();
    }
}
