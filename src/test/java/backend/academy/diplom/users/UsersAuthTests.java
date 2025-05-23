package backend.academy.diplom.users;

import backend.academy.diplom.DTO.auth.LoginRequest;
import backend.academy.diplom.DTO.auth.MailDTO;
import backend.academy.diplom.DTO.auth.RefreshRequest;
import backend.academy.diplom.TestBase;
import backend.academy.diplom.entities.user.User;
import backend.academy.diplom.repositories.auth.RefreshTokenRepository;
import backend.academy.diplom.repositories.auth.UserRepository;
import backend.academy.diplom.services.auth.EmailService;
import backend.academy.diplom.utils.PasswordUtils;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Timestamp;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@DirtiesContext
public class UsersAuthTests extends TestBase {

    @MockitoBean
    EmailService emailService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void createUserTest() throws Exception {
        User user = new User();
        user.setEmail("52Kirll@mail.ru");
        user.setPassword("52PasswordKirik228");

        assertEquals(0, userRepository.getAllUsers().size());

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());

        assertEquals(1, userRepository.getAllUsers().size());
    }

    @Test
    public void signInCorrectTest() throws Exception {
        String content = """
                {
                    "email": "test@mail.ru"
                }""";
        when(passwordUtils.generateRandomPassword(anyInt())).thenReturn("testtest");

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new LoginRequest("test@mail.ru", "testtest"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    public void badCredentialsTest() throws Exception {
        String content = """
                {
                    "email": "test@mail.ru"
                }""";
        when(passwordUtils.generateRandomPassword(anyInt())).thenReturn("testtest");

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest("test@mail.ru", "INVALID"))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Неверный email или пароль"));
    }

    @Test
    public void refreshTestCorrect() throws Exception {
        String content = """
            {
                "email": "test@mail.ru"
            }""";

        when(passwordUtils.generateRandomPassword(anyInt())).thenReturn("testtest");

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated());

        String loginResponse = mockMvc.perform(post("/api/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest("test@mail.ru", "testtest"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map<String, String> responseMap = objectMapper.readValue(loginResponse, Map.class);
        String refreshToken = responseMap.get("refreshToken");

       mockMvc.perform(post("/api/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RefreshRequest(refreshToken))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    public void refreshTestIncorrect() throws Exception {
        String content = """
            {
                "email": "test@mail.ru"
            }""";

        when(passwordUtils.generateRandomPassword(anyInt())).thenReturn("testtest");

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest("test@mail.ru", "testtest"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andReturn()
                .getResponse();

        mockMvc.perform(post("/api/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RefreshRequest("52"))))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void refreshTestExpired() throws Exception {
        String content = """
            {
                "email": "test@mail.ru"
            }""";

        when(passwordUtils.generateRandomPassword(anyInt())).thenReturn("testtest");

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated());

        String loginResponse = mockMvc.perform(post("/api/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest("test@mail.ru", "testtest"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map<String, String> responseMap = objectMapper.readValue(loginResponse, Map.class);
        String refreshToken = responseMap.get("refreshToken");
        String sql = "UPDATE engineers.refresh_token SET expire_date = ? WHERE token = ?";

        Timestamp timestamp = Timestamp.valueOf("2000-01-01 00:00:00.000000");

        jdbcTemplate.update(sql, timestamp, refreshToken);

        Integer count = jdbcTemplate.queryForObject("select count(*) from engineers.refresh_token", Integer.class);
        assertEquals(1, count);

        mockMvc.perform(post("/api/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RefreshRequest(refreshToken))))
                .andExpect(status().is4xxClientError());

        count = jdbcTemplate.queryForObject("select count(*) from engineers.refresh_token", Integer.class);
        assertEquals(0, count);
    }

    @Test
    public void requestResetEmailExistTest() throws Exception {
        String content = """
            {
                "email": "test@mail.ru"
            }""";

        when(passwordUtils.generateRandomPassword(anyInt())).thenReturn("testtest");

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/request-reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new MailDTO("test@mail.ru"))))
                .andExpect(status().isOk());

        verify(emailService, times(1)).sendResetPasswordToken(eq("test@mail.ru"), anyString());
    }

    @Test
    public void requestResetEmailDoesNotExistTest() throws Exception {
        assertThrows(ServletException.class, () ->
                mockMvc.perform(post("/api/request-reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new MailDTO("test@mail.ru")))
                )
        );
    }
}
