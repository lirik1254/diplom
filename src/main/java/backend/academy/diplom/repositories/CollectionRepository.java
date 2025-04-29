package backend.academy.diplom.repositories;

import backend.academy.diplom.entities.Collection;
import backend.academy.diplom.repositories.rowmappers.CollectionRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CollectionRepository {
    private final NamedParameterJdbcTemplate template;
    private final CollectionRowMapper collectionRowMapper;

    public void addCollection(Long userId, String name, String description) {
        String sql = """
                insert into engineers.collection(name, description, user_id)
                values (:name, :description, :userId)
                """;

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("description", description)
                        .addValue("userId", userId);

        template.update(sql ,sqlParameterSource);
    }

    public void deleteCollection(Long userId, String name) {
        String sql = """
                delete from engineers.collection
                where name = :name and
                user_id = :userId""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("userId", userId);

        template.update(sql, sqlParameterSource);
    }

    public Collection getCollection(Long userId, String name) {
        String sql = """
                select * from engineers.collection
                where name = :name
                and user_id = :userId""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("userId", userId);

        return template.query(sql, sqlParameterSource, collectionRowMapper).getFirst();
    }

    public void editCollection(Long userId, String oldName, String newName,
                               String newDescription) {
        if (newName != null && newDescription != null && !newName.isEmpty()) {
            String sql = """
                    update engineers.collection
                    set name = :newName,
                    description = :newDescription
                    where user_id = :userId
                    and name = :oldName""";

            SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                    .addValue("userId", userId)
                    .addValue("newName", newName)
                    .addValue("newDescription", newDescription)
                    .addValue("oldName", oldName);

            template.update(sql, sqlParameterSource);
        }
    }
}
