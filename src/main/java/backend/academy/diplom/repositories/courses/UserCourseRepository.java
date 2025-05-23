package backend.academy.diplom.repositories.courses;

import backend.academy.diplom.entities.user.UserCourse;
import backend.academy.diplom.repositories.rowmappers.course.UserCourseRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserCourseRepository {
    private final UserCourseRowMapper userCourseRowMapper;
    private final NamedParameterJdbcTemplate template;

    public UserCourse getUserCourseByUserAndCourseId(Long userId, Long courseId) {
        String sql = """
                select * from engineers.user_course
                where user_id = :userId and course_id = :courseId""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("courseId", courseId);

        return template.query(sql ,sqlParameterSource, userCourseRowMapper).getFirst();
    }

    public void updateLastLesson(Long userId, Long courseId, Long lessonId) {
        String sql = """
            update engineers.user_course
            set last_lesson_id = :lessonId
            where user_id = :userId and course_id = :courseId""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("lessonId", lessonId)
                        .addValue("courseId", courseId);

        template.update(sql, sqlParameterSource);
    }
}
