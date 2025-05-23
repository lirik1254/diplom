package backend.academy.diplom.courses;

import backend.academy.diplom.DTO.auth.LoginRequest;
import backend.academy.diplom.TestBase;
import backend.academy.diplom.entities.Course;
import backend.academy.diplom.repositories.courses.CourseRepository;
import backend.academy.diplom.utils.PasswordUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@DirtiesContext
public class CourseTests extends TestBase {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    public void getMainPreviewTest() throws Exception {
        String registerContent = """
        {
            "email": "test@mail.ru"
        }
    """;
        when(passwordUtils.generateRandomPassword(anyInt())).thenReturn("testtest");
        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerContent));

        LoginRequest loginReq = new LoginRequest("test@mail.ru", "testtest");
        MvcResult loginResult = mockMvc.perform(post("/api/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andReturn();

        String loginJson = loginResult.getResponse().getContentAsString();
        String accessToken = objectMapper
                .readTree(loginJson)
                .get("accessToken")
                .asText();

        jdbcTemplate.batchUpdate(
                "INSERT INTO engineers.course " +
                        "(id, name, price, duration, start_date, hours, format, who_whom, what_master, price_full) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                List.of(
                        new Object[]{1L, "Intro to Java",           "499", "4 weeks", "2025-06-10", "40", "online",  "beginners",       "Alice Ivanova", "599"},
                        new Object[]{2L, "Spring Boot Deep Dive",    "799", "6 weeks", "2025-07-01", "60", "offline", "intermediate",   "Bob Petrov",    "899"},
                        new Object[]{3L, "Hibernate & JPA Basics",   "399", "3 weeks", "2025-05-20", "30", "online",  "everyone",       "Eve Sidorova",  "449"},
                        new Object[]{4L, "Microservices in Practice","999", "8 weeks", "2025-08-15", "80", "hybrid",  "advanced",       "Carl Smirnov",  "1099"},
                        new Object[]{5L, "Kotlin for Java Devs",     "599", "5 weeks", "2025-06-25", "50", "online",  "java-developers","Dana Kuznetsova","699"}
                )
        );

        mockMvc.perform(get("/api/courses/main-preview")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
        ;
    }

    @Test
    public void getCoursesAllPreviewTest() throws Exception {
        String registerContent = """
        {
            "email": "test@mail.ru"
        }
    """;
        when(passwordUtils.generateRandomPassword(anyInt())).thenReturn("testtest");
        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerContent));

        LoginRequest loginReq = new LoginRequest("test@mail.ru", "testtest");
        MvcResult loginResult = mockMvc.perform(post("/api/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andReturn();

        String loginJson = loginResult.getResponse().getContentAsString();
        String accessToken = objectMapper
                .readTree(loginJson)
                .get("accessToken")
                .asText();

        jdbcTemplate.batchUpdate(
                "INSERT INTO engineers.course " +
                        "(id, name, price, duration, start_date, hours, format, who_whom, what_master, price_full) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                List.of(
                        new Object[]{1L, "Intro to Java",           "499", "4 weeks", "2025-06-10", "40", "online",  "beginners",       "Alice Ivanova", "599"},
                        new Object[]{2L, "Spring Boot Deep Dive",    "799", "6 weeks", "2025-07-01", "60", "offline", "intermediate",   "Bob Petrov",    "899"},
                        new Object[]{3L, "Hibernate & JPA Basics",   "399", "3 weeks", "2025-05-20", "30", "online",  "everyone",       "Eve Sidorova",  "449"},
                        new Object[]{4L, "Microservices in Practice","999", "8 weeks", "2025-08-15", "80", "hybrid",  "advanced",       "Carl Smirnov",  "1099"},
                        new Object[]{5L, "Kotlin for Java Devs",     "599", "5 weeks", "2025-06-25", "50", "online",  "java-developers","Dana Kuznetsova","699"}
                )
        );

        mockMvc.perform(get("/api/courses/all-preview")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[*].tags").exists());
    }

    @Test
    public void getCourseDetailTest() throws Exception {
        String registerContent = """
        {
            "email": "test@mail.ru"
        }
    """;
        when(passwordUtils.generateRandomPassword(anyInt())).thenReturn("testtest");
        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerContent));

        LoginRequest loginReq = new LoginRequest("test@mail.ru", "testtest");
        MvcResult loginResult = mockMvc.perform(post("/api/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andReturn();

        String loginJson = loginResult.getResponse().getContentAsString();
        String accessToken = objectMapper
                .readTree(loginJson)
                .get("accessToken")
                .asText();

        jdbcTemplate.batchUpdate(
                "INSERT INTO engineers.course " +
                        "(id, name, price, duration, start_date, hours, format, who_whom, what_master, price_full) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                List.of(
                        new Object[]{1L, "Intro to Java",           "499", "4 weeks", "2025-06-10", "40", "online",  "beginners",       "Alice Ivanova", "599"},
                        new Object[]{2L, "Spring Boot Deep Dive",    "799", "6 weeks", "2025-07-01", "60", "offline", "intermediate",   "Bob Petrov",    "899"},
                        new Object[]{3L, "Hibernate & JPA Basics",   "399", "3 weeks", "2025-05-20", "30", "online",  "everyone",       "Eve Sidorova",  "449"},
                        new Object[]{4L, "Microservices in Practice","999", "8 weeks", "2025-08-15", "80", "hybrid",  "advanced",       "Carl Smirnov",  "1099"},
                        new Object[]{5L, "Kotlin for Java Devs",     "599", "5 weeks", "2025-06-25", "50", "online",  "java-developers","Dana Kuznetsova","699"}
                )
        );


        mockMvc.perform(get("/api/courses/course-detail/2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.name",        is ("Spring Boot Deep Dive")))
                .andExpect(jsonPath("$.format",      is("offline")))
                .andExpect(jsonPath("$.price",       is("899")))
                .andExpect(jsonPath("$.duration",    is("6 weeks")))
                .andExpect(jsonPath("$.whoWhom",     is("intermediate")))
                .andExpect(jsonPath("$.whatMaster",  is("Bob Petrov")));
    }


}
