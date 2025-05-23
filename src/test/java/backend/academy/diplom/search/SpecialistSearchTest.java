package backend.academy.diplom.search;

import backend.academy.diplom.TestBase;
import backend.academy.diplom.utils.PasswordUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SpecialistSearchTest extends TestBase {

    public void init() {
        initUsers();
        initSectionAndStampsAndSoftwareSkills();
    }

    private void initUsers() {
        String sql = "INSERT INTO engineers.user " +
                "(id, name, surname) " +
                "VALUES (?, ?, ?)";

        List<Object[]> batchArgs = List.of(
                new Object[]{2L, "Шульжик","Кирилл"},
                new Object[]{3L, "Навальный", "Алексей"},
                new Object[]{4L, "Невоструев", "Роман"},
                new Object[]{5L, "Лебедев", "Алексей"},
                new Object[]{6L, "Трубочищев", "Артём"}
        );

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    private void initSectionAndStampsAndSoftwareSkills() {
        String sqlSectionAndStamps = """
                insert into engineers.user_section_and_stamp(user_id, section_and_stamp_id)
                values (?, ?)""";

        String sqlSoftwareSkills = """
                insert into engineers.user_software_skill(user_id, software_skill_id)
                values (?, ?)""";

        List<Object[]> batchArgs = List.of(
                new Object[]{2L, 1L},
                new Object[]{3L, 1L},
                new Object[]{4L, 2L},
                new Object[]{4L, 3L},
                new Object[]{5L, 3L},
                new Object[]{6L, 4L}
        );

        jdbcTemplate.batchUpdate(sqlSectionAndStamps, batchArgs);
        jdbcTemplate.batchUpdate(sqlSoftwareSkills, batchArgs);
    }


    @Test
    public void getAllSpecialistsWithoutFilters() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        init();

        mockMvc.perform(get("/api/search/specialists")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(6)));
    }

    @Test
    public void getSpecialistByNameFilter() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        init();

        mockMvc.perform(get("/api/search/specialists")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .param("name", "н"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].fullName", containsInAnyOrder(
                        "Алексей Навальный",
                        "Роман Невоструев"
                )));
    }

    @Test
    public void getSpecialistsBySectionAndStampFilterOneTag() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        init();

        mockMvc.perform(get("/api/search/specialists")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .param("sectionAndStamp", "ГП"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].fullName", containsInAnyOrder(
                        "Кирилл Шульжик",
                        "Алексей Навальный"
                )));
    }

    @Test
    public void getSpecialistsBySectionAndStampFilterMoreThanOneTag() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        init();

        mockMvc.perform(get("/api/search/specialists")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .param("sectionAndStamp", "ГП, АС"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].fullName", containsInAnyOrder(
                        "Кирилл Шульжик",
                        "Алексей Навальный",
                        "Роман Невоструев"
                )));
    }

    @Test
    public void getSpecialistsBySoftwareSkillByOneTag() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        init();

        mockMvc.perform(get("/api/search/specialists")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .param("softwareSkill", "Allplan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].fullName", containsInAnyOrder(
                        "Кирилл Шульжик",
                        "Алексей Навальный"
                )));
    }

    @Test
    public void getSpecialistsBySoftwareSkillMoreThanOneTag() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        init();

        mockMvc.perform(get("/api/search/specialists")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .param("sectionAndStamp", "Allplan, Renga"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].fullName", containsInAnyOrder(
                        "Кирилл Шульжик",
                        "Алексей Навальный",
                        "Роман Невоструев"
                )));
    }

    @Test
    public void getSpecialistsBySectionAndStampAndSoftwareSkillFilter() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        init();

        mockMvc.perform(get("/api/search/specialists")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .param("sectionAndStamp", "Allplan, Renga")
                        .param("softwareSkill", "ВК"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[*].fullName", containsInAnyOrder(
                        "Кирилл Шульжик",
                        "Алексей Навальный",
                        "Роман Невоструев",
                        "Артём Трубочищев"
                )));
    }

    @Test
    public void getSpecialistsByAllFilters() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        init();

        mockMvc.perform(get("/api/search/specialists")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .param("sectionAndStamp", "Allplan, Renga")
                        .param("softwareSkill", "ВК")
                        .param("name", "и"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].fullName", containsInAnyOrder(
                        "Кирилл Шульжик",
                        "Артём Трубочищев"
                )));
    }

}
