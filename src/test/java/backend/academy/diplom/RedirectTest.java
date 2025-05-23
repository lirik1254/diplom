package backend.academy.diplom;

import backend.academy.diplom.controllers.RedirectController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RedirectTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new RedirectController())
                .build();
    }

    @Test
    void whenGetWithParams_thenRedirectsToCustomScheme() throws Exception {
        mockMvc.perform(get("/api/redirect")
                        .param("token", "abc123")
                        .param("mail", "user@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("protimrf://reset-password?token=abc123&mail=user@example.com"));
    }

}
