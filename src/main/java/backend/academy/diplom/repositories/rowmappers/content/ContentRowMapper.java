package backend.academy.diplom.repositories.rowmappers.content;

import backend.academy.diplom.entities.Content;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ContentRowMapper implements RowMapper<Content> {
    @Override
    public Content mapRow(ResultSet rs, int rowNum) throws SQLException {
        Content content = new Content();
        content.setId(rs.getLong("id"));
        content.setContentType(rs.getString("content_type"));
        content.setContent(rs.getString("content"));
        content.setContentUrl(rs.getString("content_url"));
        content.setOrder(rs.getInt("order"));
        content.setLessonId(rs.getLong("lesson_id"));

        return content;
    }
}
