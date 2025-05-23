package backend.academy.diplom.repositories.content;

import backend.academy.diplom.entities.Content;
import backend.academy.diplom.entities.ProjectContent;
import backend.academy.diplom.repositories.rowmappers.content.ProjectContentRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProjectContentRepository {
    private final NamedParameterJdbcTemplate template;
    private final ProjectContentRowMapper projectContentRowMapper;


    public List<ProjectContent> getProjectContent(Long projectId) {
        String sql = """
                select * from engineers.project_content p
                where project_id = :projectId
                order by p.content_order""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("projectId", projectId);

        return template.query(sql, sqlParameterSource, projectContentRowMapper);
    }
}
