package backend.academy.diplom.courses;

import backend.academy.diplom.TestBase;
import backend.academy.diplom.services.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OwnedCourseTests extends TestBase {

    @MockitoBean
    private FileService fileService;

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
                new Object[]{1L, "Первый урок 1 модуля", 1L, 1L, 1},
                new Object[]{2L, "Первый урок 2 модуля", 2L, 1L, 2}
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
    public void getOwnedCourse() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        initOwned();

        mockMvc.perform(get("/api/courses/main-preview")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                        .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(3)));

        mockMvc.perform(get("/api/courses/owned")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void getCourseProgram() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        when(fileService.getLink(anyString(), anyString())).thenReturn("somelink");

        String sql = """
                update engineers.user
                set photo_path = 'photo'
                where id = 1""";

        jdbcTemplate.update(sql);
        initOwned();

        mockMvc.perform(get("/api/courses/course-program/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))

                .andExpect(jsonPath("$[0].moduleName", is("Первый модуль")))
                .andExpect(jsonPath("$[0].lessonPreviewDTOS", hasSize(1)))
                .andExpect(jsonPath("$[0].lessonPreviewDTOS[0].name", is("Первый урок 1 модуля")))

                .andExpect(jsonPath("$[1].moduleName", is("Второй модуль")))
                .andExpect(jsonPath("$[1].lessonPreviewDTOS", hasSize(1)))
                .andExpect(jsonPath("$[1].lessonPreviewDTOS[0].name", is("Первый урок 2 модуля")));
    }

    @Test
    public void getLastSeenProgramFirstLesson() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        when(fileService.getLink(anyString(), anyString())).thenReturn("somelink");

        String sql = """
                update engineers.user
                set photo_path = 'photo'
                where id = 1""";

        jdbcTemplate.update(sql);
        initOwned();

        mockMvc.perform(get("/api/courses/last-seen/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andDo(print())
                .andExpect(jsonPath("$.moduleName", is("Первый модуль")))
                .andExpect(jsonPath("$.lessonPreviewDTOS", hasSize(1)))
                .andExpect(jsonPath("$.lessonPreviewDTOS[0].name", is("Первый урок 1 модуля")));
    }

    @Test
    public void getLastSeenProgramSecondLesson() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        initOwned();

        mockMvc.perform(post("/api/lesson/update-last-lesson/2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken));

        mockMvc.perform(get("/api/courses/last-seen/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andDo(print())
                .andExpect(jsonPath("$.moduleName", is("Второй модуль")))
                .andExpect(jsonPath("$.lessonPreviewDTOS", hasSize(1)))
                .andExpect(jsonPath("$.lessonPreviewDTOS[0].name", is("Первый урок 2 модуля")));
    }

    @Test
    public void halfProgressOnStartCourse() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        initOwned();

        mockMvc.perform(get("/api/courses/owned")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].progress").value(50));
    }

    @Test
    public void fullProgressOnEndCourse() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        initOwned();

        mockMvc.perform(post("/api/lesson/update-last-lesson/2")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken));

        mockMvc.perform(get("/api/courses/owned")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].progress").value(100));
    }
}
