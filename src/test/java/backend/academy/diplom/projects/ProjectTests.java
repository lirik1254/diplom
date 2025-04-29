package backend.academy.diplom.projects;


import backend.academy.diplom.DTO.auth.LoginRequest;
import backend.academy.diplom.TestBase;
import backend.academy.diplom.utils.PasswordUtils;
import org.junit.jupiter.api.BeforeEach;
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

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@DirtiesContext
public class ProjectTests extends TestBase {

    @MockitoBean
    private PasswordUtils passwordUtils;

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
    public void getProjectsTest() throws Exception {
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

        mockMvc.perform(get("/api/projects")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].name").value("Alpha Initiative"));
    }

    @Test
    public void getNewProjectsTest() throws Exception {
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

        mockMvc.perform(get("/api/projects")
                        .param("filter", "new")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].name").value("Epsilon Prototype"))
                .andExpect(jsonPath("$[4].name").value("Delta Overhaul"));
    }

    @Test
    public void getOnly2NewProjects() throws Exception {
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

        mockMvc.perform(get("/api/projects")
                        .param("count", String.valueOf(2))
                        .param("filter", "new")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getPopularProjectsTest() throws Exception {
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

        mockMvc.perform(get("/api/projects")
                        .param("filter", "popularity")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].name").value("Delta Overhaul"));
    }
}
