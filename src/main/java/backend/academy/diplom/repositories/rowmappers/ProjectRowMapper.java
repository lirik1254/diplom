package backend.academy.diplom.repositories.rowmappers;

import backend.academy.diplom.entities.Project;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ProjectRowMapper implements RowMapper<Project> {
    @Override
    public Project mapRow(ResultSet rs, int rowNum) throws SQLException {
        Project project = new Project();
        project.setId(rs.getLong("id"));
        project.setName(rs.getString("name"));
        project.setAuthorId(rs.getLong("author_id"));
        project.setLikeCount(rs.getInt("like_count"));
        project.setPhotoUrl(rs.getString("photo_url"));

        return project;
    }
}
