//package backend.academy.diplom.repositories.lesson;
//
//import backend.academy.diplom.entities.lesson.LessonUser;
//import backend.academy.diplom.repositories.rowmappers.course.CourseNameRowMapper;
//import backend.academy.diplom.repositories.rowmappers.lesson.LessonUserRowMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import org.springframework.jdbc.core.namedparam.SqlParameterSource;
//import org.springframework.stereotype.Repository;
//
////@Repository
////@RequiredArgsConstructor
////public class LessonUserRepository {
////
////    private final NamedParameterJdbcTemplate template;
////    private final LessonUserRowMapper lessonUserRowMapper;
////    private final CourseNameRowMapper courseNameRowMapper;
////
////    public LessonUser getIsHereByCourseNameAndUserId(String courseName, Long userId) {
////        String sql = """
////                select lu.lesson_id, user_id, is_here from engineers.lesson_user lu
////                join engineers.lesson l on lu.lesson_id = l.id
////                join engineers.module m on l.module_id = m.id
////                join engineers.course_module cm on cm.module_id = m.id
////                join engineers.course c on c.id = cm.course_id
////                         where c.name = :courseName
////                           and lu.user_id = :userId
////                and is_here = true""";
////
////        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
////                .addValue("courseName", courseName)
////                .addValue("userId", userId);
////
////        try {
////            return template.query(sql, sqlParameterSource, lessonUserRowMapper).getFirst();
////        } catch (Exception e) {
////            return null;
////        }
////    }
////
////    public void updateLastSeenCourse(Long userId, Long lessonId) {
////        String courseName = getCourseNameByLessonId(lessonId);
////        LessonUser lessonUser = getIsHereByCourseNameAndUserId(courseName, userId);
////
////        if (lessonUser == null) {
////            String updateSql = """
////                    update engineers.lesson_user set
////                    is_here = true where user_id = :userId
////                    and lesson_id = :lessonId""";
////
////            SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
////                    .addValue("lessonId", lessonId)
////                    .addValue("userId", userId);
////
////            template.update(updateSql, sqlParameterSource);
////        } else if (lessonUser.getLessonId() < lessonId) {
////            String updateSql = """
////                    update engineers.lesson_user set
////                    is_here = true where user_id = :userId
////                    and lesson_id = :lessonId;""";
////
////            SqlParameterSource updateSqlParSource = new MapSqlParameterSource()
////                    .addValue("userId", userId)
////                    .addValue("lessonId", lessonId);
////
////            template.update(updateSql, updateSqlParSource);
////
////            String updateLastSql = """
////                    update engineers.lesson_user set
////                    is_here = false where user_id = :userId
////                    and lesson_id = :lessonId""";
////
////            SqlParameterSource updateLastSqlSource = new MapSqlParameterSource()
////                    .addValue("userId", userId)
////                    .addValue("lessonId", lessonUser.getLessonId());
////
////            template.update(updateLastSql, updateLastSqlSource);
////        }
////    }
////
////    public String getCourseNameByLessonId(Long lessonId) {
////        String sql = """
////                select c.name from engineers.lesson l join engineers.module m on l.module_id = m.id
////                join engineers.course_module cm on cm.module_id = m.id
////                join engineers.course c on c.id = cm.course_id
////                where l.id = :lessonId""";
////
////        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("lessonId",
////                lessonId);
////
////        return template.query(sql, sqlParameterSource, courseNameRowMapper).getFirst();
////    }
////}
