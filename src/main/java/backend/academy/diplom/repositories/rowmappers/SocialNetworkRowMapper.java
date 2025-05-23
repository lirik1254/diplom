package backend.academy.diplom.repositories.rowmappers;

import backend.academy.diplom.entities.SocialNetwork;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class SocialNetworkRowMapper implements RowMapper<SocialNetwork> {
    @Override
    public SocialNetwork mapRow(ResultSet rs, int rowNum) throws SQLException {
        SocialNetwork socialNetwork = new SocialNetwork();

        socialNetwork.setId(rs.getLong("id"));
        socialNetwork.setUserId(rs.getLong("user_id"));
        socialNetwork.setSocialNetwork(rs.getString("social_network"));

        return socialNetwork;
    }
}
