package backend.academy.diplom.repositories;

import backend.academy.diplom.entities.Company;
import backend.academy.diplom.repositories.rowmappers.CollectionRowMapper;
import backend.academy.diplom.repositories.rowmappers.CompanyRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CompanyRepository {
    private final NamedParameterJdbcTemplate template;
    private final CompanyRowMapper companyRowMapper;

    public List<Company> getCompanies() {
        String sql = "select * from engineers.companies";

        return template.query(sql, companyRowMapper);
    }
}
