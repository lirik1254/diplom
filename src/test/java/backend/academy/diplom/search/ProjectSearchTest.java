package backend.academy.diplom.search;

import backend.academy.diplom.DTO.search.RecommendDTO;
import backend.academy.diplom.TestBase;
import backend.academy.diplom.clients.RecommendClient;
import backend.academy.diplom.entities.Project;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProjectSearchTest extends TestBase {

    @MockitoBean
    private RecommendClient recommendClient;

    @BeforeEach
    public void beforeEach() {
        List<RecommendDTO> projectIds = new ArrayList<>(
                List.of(new RecommendDTO(1L),
                        new RecommendDTO(2L),
                        new RecommendDTO(3L),
                        new RecommendDTO(4L),
                        new RecommendDTO(5L))
        );
        when(recommendClient.getRecommendProjectIds(1L)).thenReturn(projectIds);
    }

    public void init() {
        initUsers();
        initProjects();
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

    private void initProjects() {
        String sql = "INSERT INTO engineers.project " +
                "(id, name, author_id) " +
                "VALUES (?, ?, ?)";

        List<Object[]> batchArgs = List.of(
                new Object[]{1L, "Проект первый", 2L},
                new Object[]{2L, "Проект второй", 3L},
                new Object[]{3L, "Проект третий", 4L},
                new Object[]{4L, "Проект четвертый", 5L},
                new Object[]{5L, "Проект пятый", 6L}
        );

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    private void initSectionAndStampsAndSoftwareSkills() {
        String sqlSectionAndStamps = """
                insert into engineers.project_section_and_stamp(project_id, section_and_stamp_id)
                values (?, ?)""";

        String sqlSoftwareSkills = """
                insert into engineers.project_software_skill(project_id, software_skill_id)
                values (?, ?)""";

        List<Object[]> batchArgs = List.of(
                new Object[]{1L, 1L},
                new Object[]{2L, 1L},
                new Object[]{3L, 2L},
                new Object[]{3L, 3L},
                new Object[]{4L, 3L},
                new Object[]{5L, 4L}
        );

        jdbcTemplate.batchUpdate(sqlSectionAndStamps, batchArgs);
        jdbcTemplate.batchUpdate(sqlSoftwareSkills, batchArgs);
    }

    @Test
    public void getAllProjectsWithoutFilters() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        init();

        mockMvc.perform(get("/api/search/projects")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @Test
    public void getProjectByNameFilter() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        init();

        mockMvc.perform(get("/api/search/projects")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .param("name", "ы"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(
                        "Проект первый",
                        "Проект четвертый",
                        "Проект пятый"
                )));
    }

    @Test
    public void getProjectsBySectionAndStampFilterOneTag() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        init();

        mockMvc.perform(get("/api/search/projects")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .param("sectionAndStamp", "ГП"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(
                        "Проект первый",
                        "Проект второй"
                )));
    }

    @Test
    public void getProjectsBySectionAndStampFilterMoreThanOneTag() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        init();

        mockMvc.perform(get("/api/search/projects")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .param("sectionAndStamp", "ГП, АС"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(
                        "Проект первый",
                        "Проект второй",
                        "Проект третий"
                )));
    }

    @Test
    public void getProjectsBySoftwareSkillByOneTag() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        init();

        mockMvc.perform(get("/api/search/projects")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .param("softwareSkill", "Allplan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(
                        "Проект первый",
                        "Проект второй"
                )));
    }

    @Test
    public void getProjectsBySoftwareSkillMoreThanOneTag() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        init();

        mockMvc.perform(get("/api/search/projects")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .param("sectionAndStamp", "Allplan, Renga"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(
                        "Проект первый",
                        "Проект второй",
                        "Проект третий"
                )));
    }

    @Test
    public void getProjectsBySectionAndStampAndSoftwareSkillFilter() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        init();

        mockMvc.perform(get("/api/search/projects")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .param("sectionAndStamp", "Allplan, Renga")
                        .param("softwareSkill", "ВК"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(
                        "Проект первый",
                        "Проект второй",
                        "Проект третий",
                        "Проект пятый"
                )));
    }

    @Test
    public void getSpecialistsByAllFilters() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        init();

        mockMvc.perform(get("/api/search/projects")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .param("sectionAndStamp", "Allplan, Renga")
                        .param("softwareSkill", "ВК")
                        .param("name", "ы"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(
                        "Проект первый",
                        "Проект пятый"
                )));
    }
}
