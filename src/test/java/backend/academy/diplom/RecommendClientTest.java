package backend.academy.diplom;

import backend.academy.diplom.DTO.search.RecommendDTO;
import backend.academy.diplom.clients.RecommendClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.client.RestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecommendClientTest {
    @RegisterExtension
    static WireMockExtension wm = WireMockExtension.newInstance()
            .options(wireMockConfig().port(8082))
            .build();

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void getRecommendProjectIds_returnsDtoList() throws Exception {
        List<RecommendDTO> mockList = List.of(
                new RecommendDTO(1L),
                new RecommendDTO(2L)
        );
        String jsonBody = mapper.writeValueAsString(mockList);

        wm.stubFor(get(urlEqualTo("/recommend/42"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonBody)));

        RestClient restClient = RestClient.builder()
                .baseUrl("http://localhost:8082")
                .build();

        RecommendClient client = new RecommendClient(restClient);

        List<RecommendDTO> result = client.getRecommendProjectIds(42L);

        assertEquals(2, result.size(), "Должно вернуться два элемента");
    }
}
