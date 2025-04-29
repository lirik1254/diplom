package backend.academy.diplom.repositories.rowmappers;

import backend.academy.diplom.entities.Collection;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CollectionRowMapper implements RowMapper<Collection> {
    @Override
    public Collection mapRow(ResultSet rs, int rowNum) throws SQLException {
        Collection collection = new Collection();
        collection.setId(rs.getLong("id"));
        collection.setName(rs.getString("name"));
        collection.setDescription(rs.getString("description"));

        return collection;
    }
}
