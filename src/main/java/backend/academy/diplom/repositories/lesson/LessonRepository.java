package backend.academy.diplom.repositories.lesson;

import backend.academy.diplom.entities.lesson.Lesson;
import backend.academy.diplom.repositories.rowmappers.lesson.LessonRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class LessonRepository {
    private final LessonRowMapper lessonRowMapper;
    private final NamedParameterJdbcTemplate template;

    public List<Lesson> getLessonsByModuleId(Long moduleId) {
        String sql = """
                select * from engineers.lesson
                where module_id = :moduleId""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("moduleId", moduleId);

        return template.query(sql, sqlParameterSource, lessonRowMapper);
    }

    public Lesson getLessonByLessonId(Long lessonId) {
        String sql = """
                select * from engineers.lesson
                where id = :lessonId""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("lessonId", lessonId);

        return template.query(sql, sqlParameterSource, lessonRowMapper).getFirst();
    }



//    public List<Lesson> getIsHereLesson(Long moduleId, Long userId) {
//        String sql = """
//                select l.id, name, module_id, author_id from engineers.lesson l
//                join engineers.lesson_user lu on l.id = lu.lesson_id
//                where module_id = :moduleId and user_id = :userId and is_here = true
//                """;
//
//        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
//                .addValue("userId", userId)
//                .addValue("moduleId", moduleId);
//
//        return template.query(sql, sqlParameterSource, lessonRowMapper);
//    }

}
