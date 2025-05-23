package backend.academy.diplom.repositories.rowmappers;

import backend.academy.diplom.entities.Company;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CompanyRowMapper implements RowMapper<Company> {
    @Override
    public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
        Company company = new Company();
        company.setId(rs.getLong("id"));
        company.setName(rs.getString("name"));
        company.setStatus(rs.getString("status"));
        company.setCity(rs.getString("city"));
        company.setPhotoPath(rs.getString("photo_path"));

        return company;
    }
}
