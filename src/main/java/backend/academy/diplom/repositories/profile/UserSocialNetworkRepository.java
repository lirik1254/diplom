package backend.academy.diplom.repositories.profile;

import backend.academy.diplom.entities.SocialNetwork;
import backend.academy.diplom.repositories.rowmappers.SocialNetworkRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserSocialNetworkRepository {
    private final NamedParameterJdbcTemplate template;
    private final SocialNetworkRowMapper socialNetworkRowMapper;

    public void addSocialNetwork(Long userId, String socialNetwork) {
        String addSocialNetwork = """
                insert into engineers.user_social_network(user_id, social_network)
                values (:userId, :socialNetwork)""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("socialNetwork", socialNetwork);

        try {
            template.update(addSocialNetwork, sqlParameterSource);
        } catch (Exception e) {
            log.info("Попытка одинаковой вставки соц сетей");
        }
    }

    public void deleteByUserId(Long userId) {
        String deleteSql = """
                delete from engineers.user_social_network
                where user_id = :userId""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("userId", userId);

        template.update(deleteSql, sqlParameterSource);
    }

    public List<String> getSocialNetworkByUserId(Long userId) {
        String getSocialNetwork = """
                select * from engineers.user_social_network
                where user_id = :userId""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("userId", userId);

        return template.query(getSocialNetwork, sqlParameterSource, socialNetworkRowMapper)
                .stream().map(SocialNetwork::getSocialNetwork)
                .toList();
    }
}
