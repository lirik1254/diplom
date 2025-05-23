package backend.academy.diplom.favourites;

import backend.academy.diplom.DTO.auth.LoginRequest;
import backend.academy.diplom.TestBase;
import backend.academy.diplom.courses.CourseTests;
import backend.academy.diplom.utils.PasswordUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@DirtiesContext
public class FavouritesTests extends TestBase {

    public void init() {
        String sql = "INSERT INTO engineers.project " +
                "(id, name, author_id, like_count, photo_url, date_time) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        List<Object[]> batchArgs = List.of(
                new Object[]{
                        1L,
                        "Alpha Initiative",
                        1L,
                        5,
                        "https://example.com/photos/alpha.jpg",
                        Timestamp.valueOf(LocalDateTime.of(2025, 4, 1, 10, 15))
                },
                new Object[]{
                        2L,
                        "Beta Launch",
                        1L,
                        12,
                        "https://example.com/photos/beta.png",
                        Timestamp.valueOf(LocalDateTime.of(2025, 3, 20, 14, 30))
                },
                new Object[]{
                        3L,
                        "Gamma Revamp",
                        1L,
                        7,
                        "https://example.com/photos/gamma.jpeg",
                        Timestamp.valueOf(LocalDateTime.of(2025, 2, 10, 9, 0))
                },
                new Object[]{
                        4L,
                        "Delta Overhaul",
                        1L,
                        20,
                        "https://example.com/photos/delta.jpg",
                        Timestamp.valueOf(LocalDateTime.of(2025, 1, 5, 16, 45))
                },
                new Object[]{
                        5L,
                        "Epsilon Prototype",
                        1L,
                        0,
                        "https://example.com/photos/epsilon.png",
                        Timestamp.valueOf(LocalDateTime.of(2025, 4, 15, 12, 0))
                }
        );

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    @Test
    public void putFavouritesTest() throws Exception {
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

        init();

        mockMvc.perform(post("/api/favourites/3")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk());

        String sql = "SELECT COUNT(*) FROM engineers.favourites WHERE user_id = ? AND project_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, 1, 3);
        assertThat(count).isEqualTo(1);

        mockMvc.perform(get("/api/favourites/check/3")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        mockMvc.perform(get("/api/favourites/check/4")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    public void toggleFavouritesTest() throws Exception {
        // 1) Регистрация и авторизация (как в вашем putFavouritesTest)
        String registerContent = """
                {
                    "email": "test@mail.ru"
                }
                """;
        when(passwordUtils.generateRandomPassword(anyInt())).thenReturn("testtest");
        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerContent))
                .andExpect(status().isCreated());

        LoginRequest loginReq = new LoginRequest("test@mail.ru", "testtest");
        String loginJson = mockMvc.perform(post("/api/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        String accessToken = objectMapper.readTree(loginJson).get("accessToken").asText();

        init(); // ваше окружение

        // 2) Первый вызов — добавление
        mockMvc.perform(post("/api/favourites/3")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk());
        Integer countAfterAdd = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM engineers.favourites WHERE user_id = ? AND project_id = ?",
                Integer.class, 1, 3);
        assertThat(countAfterAdd).isEqualTo(1);
        mockMvc.perform(get("/api/favourites/check/3")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // 3) Второй вызов — удаление
        mockMvc.perform(post("/api/favourites/3")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk());
        Integer countAfterDelete = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM engineers.favourites WHERE user_id = ? AND project_id = ?",
                Integer.class, 1, 3);
        assertThat(countAfterDelete).isEqualTo(0);
        mockMvc.perform(get("/api/favourites/check/3")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}
