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

    public List<Course> getAlLCoursesByUserId(Long userId) {
        String sql = """
                select * from engineers.course
                where id in (select course_id from engineers.user_course where user_id = :userId)""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("userId", userId);

        return template.query(sql, sqlParameterSource, courseRowMapper);
    }

//    public List<Course> getAllCoursesByEmail(String email) {
//        String getCourseQuery = """
//                select * from engineers.course c
//                join engineers.user_course uc on c.id = uc.course_id
//                join engineers.user u on u.id = uc.user_id
//                where email = :email""";
//
//        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("email", email);
//
//        return template.query(getCourseQuery, sqlParameterSource, courseRowMapper);
//    }

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

    public Course getCourseByLessonId(Long lessonId) {
        String sql = """
                select * from engineers.course where id = (
                select c.id from engineers.lesson l
                join engineers.module m on l.module_id = m.id
                join engineers.course c on m.course_id = c.id
                where l.id = :lessonId)""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("lessonId", lessonId);

        return template.query(sql, sqlParameterSource, courseRowMapper).getFirst();
    }
}
