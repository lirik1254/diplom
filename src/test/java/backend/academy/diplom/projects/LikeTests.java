package backend.academy.diplom.projects;

import backend.academy.diplom.DTO.auth.LoginRequest;
import backend.academy.diplom.TestBase;
import backend.academy.diplom.entities.Project;
import backend.academy.diplom.repositories.ProjectRepository;
import backend.academy.diplom.utils.PasswordUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@DirtiesContext
public class LikeTests extends TestBase {
    @MockitoBean
    private PasswordUtils passwordUtils;
    @Autowired
    private ProjectRepository projectRepository;

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
    public void checkIsLikeFalseTest() throws Exception {
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

        mockMvc.perform(get("/api/projects/is-like/3")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string("false"))
                .andDo(print());
    }

    @Test
    public void checkIsLikeTrue() throws Exception {
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
        jdbcTemplate.update("insert into engineers.project_user_like(project_id, user_id) VALUES " +
                "(3, 1)");

        mockMvc.perform(get("/api/projects/is-like/3")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andDo(print());
    }

    @Test
    public void likeProjectTest() throws Exception {
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
        mockMvc.perform(post("/api/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)));

        String registerContentSecond = """
        {
            "email": "test2@mail.ru"
        }
    """;
        when(passwordUtils.generateRandomPassword(anyInt())).thenReturn("testtest");
        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerContentSecond));

        LoginRequest loginReqSecond = new LoginRequest("test2@mail.ru", "testtest");
        MvcResult loginResultSecond = mockMvc.perform(post("/api/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReqSecond)))
                .andReturn();

        String loginJsonSecond = loginResultSecond.getResponse().getContentAsString();
        String accessTokenSecond = objectMapper
                .readTree(loginJsonSecond)
                .get("accessToken")
                .asText();

        init();

        Project betaLaunchProject = projectRepository.getAllProjects()
                        .stream().filter(s -> s.getName().equals("Beta Launch"))
                        .toList().getFirst();

        assertEquals(12, betaLaunchProject.getLikeCount());

        mockMvc.perform(post("/api/projects/like/2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessTokenSecond))
                .andExpect(status().isOk());

        betaLaunchProject = projectRepository.getAllProjects()
                .stream().filter(s -> s.getName().equals("Beta Launch"))
                .toList().getFirst();
        assertEquals(13, betaLaunchProject.getLikeCount());

        String sql = "SELECT COUNT(*) FROM engineers.notification_user WHERE user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, 1);
        assertThat(count).isGreaterThan(0);
    }

    @Test
    public void ownLikeTest() throws Exception {
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

        mockMvc.perform(post("/api/projects/like/2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk());

        String sql = "SELECT COUNT(*) FROM engineers.notification_user WHERE user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, 1);
        assertThat(count).isEqualTo(0);
    }

    @Test
    public void getProjectLikeCount() throws Exception {
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

        mockMvc.perform(get("/api/projects/like-count/2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string("12"));

    }

    @Test
    public void retLike() throws Exception {
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

        mockMvc.perform(post("/api/projects/like/5")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk());

        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        mockMvc.perform(get("/api/projects/like/5")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0]").value(hasKey("photoPath")))
                .andExpect(jsonPath("$[0]").value(hasKey("fullName")));
    }
}
