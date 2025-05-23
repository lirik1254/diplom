package backend.academy.diplom.repositories;

import backend.academy.diplom.entities.Module;
import backend.academy.diplom.repositories.rowmappers.ModuleRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ModuleRepository {
    private final ModuleRowMapper moduleRowMapper;
    private final NamedParameterJdbcTemplate template;

    public List<Module> getModulesByCourseId(Long courseId) {
        String sql = """
                select * from engineers.module
                where course_id = :courseId""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("courseId", courseId);

        return template.query(sql ,sqlParameterSource, moduleRowMapper);
    }

    public Module getModuleByModuleId(Long moduleId) {
        String sql = """
                select * from engineers.module
                where id = :moduleId""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("moduleId", moduleId);

        return template.query(sql, sqlParameterSource, moduleRowMapper).getFirst();
    }

//    public List<Module> getModulesByCourseName(String courseName) {
//        String sql = """
//                select m.id, m.number, m.name from engineers.module m
//                join engineers.course_module cm on m.id = cm.module_id
//                join engineers.course c on c.id = cm.course_id
//                where c.name = :courseName""";
//
//        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("courseName",
//                courseName);
//
//        return template.query(sql, sqlParameterSource, moduleRowMapper);
//    }
//
//    public List<Module> getIsHereModuleByCourseNameAndUserId(String courseName, Long userId) {
//        String sql = """
//                select m.id, m.number, m.name from engineers.module m
//                join engineers.course_module cm on m.id = cm.module_id
//                join engineers.course c on c.id = cm.course_id
//                join engineers.lesson l on l.module_id = m.id
//                join engineers.lesson_user lu on lu.lesson_id = l.id
//                where lu.user_id = :userId and lu.is_here = true
//                and c.name = :courseName""";
//
//        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
//                .addValue("courseName", courseName)
//                .addValue("userId", userId);
//
//        try {
//            return template.query(sql, sqlParameterSource, moduleRowMapper);
//        } catch (Exception e) {
//            return null;
//        }
//    }
}
