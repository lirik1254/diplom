package backend.academy.diplom.repositories;

import backend.academy.diplom.DTO.ProjectDTO;
import backend.academy.diplom.entities.Project;
import backend.academy.diplom.repositories.rowmappers.ProjectRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ProjectRepository {
    private final NamedParameterJdbcTemplate template;
    private final JdbcTemplate jdbcTemplate;
    private final ProjectRowMapper projectRowMapper;

    public List<Project> getAllProjects() {
        String sql = """
                select * from engineers.project""";

        try {
            return template.query(sql, projectRowMapper);
        } catch (Exception e) {
            return null;
        }
    }

    public void updateLike(Long userId, Long projectId) {
        jdbcTemplate.queryForObject(
                "SELECT like_count FROM engineers.project WHERE id = ? FOR UPDATE",
                Integer.class,
                projectId
        );
        String checkSql = "SELECT COUNT(*) FROM engineers.project_user_like WHERE project_id = ? AND user_id = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, projectId, userId);

        if (count != null && count > 0) {
            String updateUnlikeSql = "UPDATE engineers.project SET like_count = like_count - 1 WHERE id = ?";
            jdbcTemplate.update(updateUnlikeSql, projectId);

            String deleteSql = "DELETE FROM engineers.project_user_like WHERE project_id = ? AND user_id = ?";
            jdbcTemplate.update(deleteSql, projectId, userId);
        } else {
            String updateLikeSql = "UPDATE engineers.project SET like_count = like_count + 1 WHERE id = ?";
            jdbcTemplate.update(updateLikeSql, projectId);

            String insertSql = "INSERT INTO engineers.project_user_like(project_id, user_id) VALUES(?, ?)";
            jdbcTemplate.update(insertSql, projectId, userId);
        }
    }

    public String getProjectNameById(Long projectId) {
        String sql = """
                select p.name from engineers.project p
                where p.id = :projectId""";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("projectId", projectId);

        return template.queryForObject(sql, sqlParameterSource, String.class);
    }

    public Long getAuthorIdByProjectId(Long projectId) {
        String sql = """
                select p.author_id from engineers.project p
                where id = :projectId""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("projectId", projectId);

        return template.queryForObject(sql, sqlParameterSource, Long.class);
    }

    public List<Project> getPopularityProjects(Integer count) {
        String sql = """
                SELECT\s
                    p.id,
                    p.name,
                    p.author_id,
                    p.like_count,
                    p.photo_url,
                    p.date_time,
                    (p.like_count + COUNT(f.project_id)) AS popularity
                FROM\s
                    engineers.project p
                LEFT JOIN\s
                    engineers.favourites f ON p.id = f.project_id
                GROUP BY\s
                    p.id
                ORDER BY\s
                    popularity DESC""";

        List<Project> retAnswer = template.query(sql, projectRowMapper);
        if (count != null) {
            return retAnswer.stream().limit(count).toList();
        }
        return retAnswer;
    }

    public List<Project> getNewProjects(Integer count) {
        if (count != null) {
            String sql = """
                    select * from engineers.project
                    order by date_time desc
                    limit :count""";
            SqlParameterSource sqlParameterSource = new MapSqlParameterSource("count", count);
            return template.query(sql, sqlParameterSource, projectRowMapper);
        }
        String sql = """
                select * from engineers.project
                order by date_time desc""";

        return template.query(sql, projectRowMapper);
    }

    public Boolean isLike(Long projectId, Long userId) {
        String sql = """
                select exists(select 1 from engineers.project_user_like where project_id = :projectId
                and user_id = :userId)""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("projectId", projectId)
                .addValue("userId", userId);

        return template.queryForObject(sql, sqlParameterSource, Boolean.class);
    }

    public Integer getProjectLikeCount(Long projectId) {
        String sql = """
                select like_count from engineers.project
                where id = :projectId""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("projectId", projectId);

        return template.queryForObject(sql, sqlParameterSource, Integer.class);
    }

    public List<Project> getProjectsByAuthorId(Long authorId) {
        String sql = """
                select * from engineers.project
                where author_id = :authorId""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("authorId", authorId);

        return template.query(sql, sqlParameterSource, projectRowMapper);
    }
}
