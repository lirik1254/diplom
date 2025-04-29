package backend.academy.diplom.repositories.rowmappers.course;

import backend.academy.diplom.entities.Course;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CourseRowMapper implements RowMapper<Course> {
    @Override
    public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
        Course course = new Course();
        course.setId(rs.getLong("id"));
        course.setName(rs.getString("name"));
        course.setPrice(rs.getString("price"));
        course.setDuration(rs.getString("duration"));
        course.setStartDate(rs.getString("start_date"));
        course.setHours(rs.getString("hours"));
        course.setFormat(rs.getString("format"));
        course.setWhoWhom(rs.getString("who_whom"));
        course.setPriceFull(rs.getString("price_full"));
        course.setWhatMaster(rs.getString("what_master"));
        return course;
    }
}
