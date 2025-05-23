package backend.academy.diplom.repositories;

import backend.academy.diplom.entities.Project;
import backend.academy.diplom.repositories.rowmappers.ProjectRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class FavouritesRepository {
    private final NamedParameterJdbcTemplate template;
    private final JdbcTemplate jdbcTemplate;
    private final ProjectRowMapper projectRowMapper;

    public void updateFavourites(Long userId, Long projectId) {
        String checkSql = "SELECT COUNT(*) FROM engineers.favourites WHERE user_id = ? AND project_id = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, userId, projectId);

        if (count != null && count > 0) {
            String deleteSql = "DELETE FROM engineers.favourites WHERE user_id = ? AND project_id = ?";
            jdbcTemplate.update(deleteSql, userId, projectId);
        } else {
            String insertSql = "INSERT INTO engineers.favourites (user_id, project_id) VALUES (?, ?)";
            jdbcTemplate.update(insertSql, userId, projectId);
        }
    }

//    public List<Project> getAllByUserId(Long userId) {
//        String sql = """
//                select p.id, name, author_id, like_count, photo_url from engineers.favourites f
//                join engineers.project p on f.project_id = p.id
//                where f.user_id = :userId""";
//
//        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("userId", userId);
//
//        return template.query(sql, sqlParameterSource, projectRowMapper);
//    }

    public Boolean checkFavourites(Long userId, Long projectId) {
        String sql = """
                select exists(select 1 from engineers.favourites where project_id = :projectId
                and user_id = :userId)""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("projectId", projectId);

        return template.queryForObject(sql, sqlParameterSource, Boolean.class);
    }
}
