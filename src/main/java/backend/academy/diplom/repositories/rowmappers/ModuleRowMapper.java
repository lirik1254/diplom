package backend.academy.diplom.repositories.rowmappers;

import backend.academy.diplom.entities.Module;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ModuleRowMapper implements RowMapper<Module> {
    @Override
    public Module mapRow(ResultSet rs, int rowNum) throws SQLException {
        Module module = new Module();
        module.setId(rs.getLong("id"));
        module.setNumber(rs.getInt("number"));
        module.setName(rs.getString("name"));

        return module;
    }
}
