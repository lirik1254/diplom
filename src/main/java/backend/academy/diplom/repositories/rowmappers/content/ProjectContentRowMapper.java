package backend.academy.diplom.repositories.rowmappers.content;

import backend.academy.diplom.entities.ProjectContent;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ProjectContentRowMapper implements RowMapper<ProjectContent> {
    @Override
    public ProjectContent mapRow(ResultSet rs, int rowNum) throws SQLException {
        ProjectContent projectContent = new ProjectContent();

        projectContent.setId(rs.getLong("id"));
        projectContent.setContentType(rs.getString("content_type"));
        projectContent.setContent(rs.getString("content"));
        projectContent.setContentUrl(rs.getString("content_url"));
        projectContent.setContentOrder(rs.getInt("content_order"));
        projectContent.setProjectId(rs.getLong("project_id"));

        return projectContent;
    }
}
