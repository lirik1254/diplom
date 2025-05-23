package backend.academy.diplom.courses;

import backend.academy.diplom.TestBase;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LessonTests extends TestBase {
    private void initCourses() {
        String sql = """
        insert into engineers.course(id, name)
        values (?, ?)""";

        List<Object[]> batchArgs = List.of(
                new Object[]{1L, "Первый курс"},
                new Object[]{2L, "Второй курс"},
                new Object[]{3L, "Третий курс"}
        );

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    private void initModules() {
        String sql = """
                insert into engineers.module(id, number, name, course_id)
                values (?, ?, ?, ?)""";

        List<Object[]> batchArgs = List.of(
                new Object[]{1L, 1, "Первый модуль", 1L},
                new Object[]{2L, 2, "Второй модуль", 1L}
        );

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    private void initLessons() {
        String sql = """
                insert into engineers.lesson(id, name, module_id, author_id, number)
                values (?, ?, ?, ?, ?)""";

        List<Object[]> batchArgs = List.of(
                new Object[]{1L, "Урок 1.1 Первый урок 1 модуля", 1L, 1L, 1},
                new Object[]{2L, "Урок 2.1 Первый урок 2 модуля", 2L, 1L, 2}
        );

        jdbcTemplate.batchUpdate(sql ,batchArgs);
    }

    private void initLessonContent() {
        String sql = """
                insert into engineers.lesson_content(id, lesson_text, lesson_id)
                values (?, ?, ?)""";

        List<Object[]> batchArgs = Collections.singletonList(
                new Object[]{1L, "Контент второго урока", 2L}
        );

        jdbcTemplate.batchUpdate(sql ,batchArgs);
    }

    private void init() {
        initCourses();
        initModules();
        initLessons();
        initLessonContent();
    }

    private void initOwned() {
        init();
        String sql = """
                insert into engineers.user_course(user_id, course_id, last_lesson_id)
                values(1, 1, 1);""";

        jdbcTemplate.update(sql);
    }


    @Test
    public void getLessonContent() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        initOwned();

        mockMvc.perform(get("/api/lesson/2/content")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonId").value(2))
                .andExpect(jsonPath("$.lessonText").value("Контент второго урока"))
                .andExpect(jsonPath("$.lessonNameWithNumber").value("Урок 2.1"));
    }

    private void initExtraLesson() {
        String sql = """
                insert into engineers.lesson(id, name, module_id, author_id, number)
                values (?, ?, ?, ?, ?)""";

        List<Object[]> batchArgs = Collections.singletonList(
                new Object[]{3L, "Урок 1.2 Второй урок 1 модуля", 1L, 1L, 2}
        );

        jdbcTemplate.batchUpdate(sql ,batchArgs);
    }

    @Test
    public void getBeforeLessonIdTest() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        initOwned();

        initExtraLesson();

        mockMvc.perform(get("/api/lesson/before-lesson/2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonId").value(3));
    }

    @Test
    public void getBeforeFirstLessonIdTest() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        initOwned();

        initExtraLesson();

        mockMvc.perform(get("/api/lesson/before-lesson/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonId").value(1));
    }

    @Test
    public void getAfterLessonIdTest() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        initOwned();

        initExtraLesson();

        mockMvc.perform(get("/api/lesson/after-lesson/3")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonId").value(2));
    }

    @Test
    public void getAfterLastLessonIdTest() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        initOwned();

        initExtraLesson();

        mockMvc.perform(get("/api/lesson/after-lesson/2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonId").value(2));
    }
}
