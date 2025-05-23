package backend.academy.diplom.clients;

import backend.academy.diplom.DTO.search.RecommendDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RecommendClient {
    private final RestClient restClient;

    public List<RecommendDTO> getRecommendProjectIds(Long userId) {
        try {
            return restClient
                    .get()
                    .uri("/recommend/{userId}", userId)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (Exception e) {
            return List.of();
        }
    }
}
