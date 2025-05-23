package backend.academy.diplom.search;

import backend.academy.diplom.TestBase;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CompanySearchTest extends TestBase {

    private void init() {
        String sql = "INSERT INTO engineers.companies " +
                "(id, name) " +
                "VALUES (?, ?)";

        List<Object[]> batchArgs = List.of(
                new Object[]{2L, "Т-Банк"},
                new Object[]{3L, "Контур"},
                new Object[]{4L, "Гриндата"},
                new Object[]{5L, "ВТБ"},
                new Object[]{6L, "Форсайт"}
        );

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    @Test
    public void getAllCompaniesWithoutFilters() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        init();

        mockMvc.perform(get("/api/search/companies")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @Test
    public void getCompaniesByNameFilter() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        init();

        mockMvc.perform(get("/api/search/companies")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .param("name", "-"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Т-Банк"))
                );
    }

}
