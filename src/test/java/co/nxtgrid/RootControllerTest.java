package co.nxtgrid;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class RootControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void root_returnsServiceIndex() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("nxt-sts"))
            .andExpect(jsonPath("$.version").value("1.0.0"))
            .andExpect(jsonPath("$.description").isNotEmpty())
            .andExpect(jsonPath("$.endpoints.token").value("POST /token"))
            .andExpect(jsonPath("$.endpoints.health").value("GET /actuator/health"))
            .andExpect(jsonPath("$.endpoints.openapi").value("GET /v3/api-docs"))
            .andExpect(jsonPath("$.endpoints.swaggerUi").value("GET /swagger"));
    }

    @Test
    void getOnToken_returnsMethodNotAllowed() throws Exception {
        mockMvc.perform(get("/token"))
            .andExpect(status().isMethodNotAllowed())
            .andExpect(jsonPath("$.error").value("Method not allowed for this endpoint"));
    }

    @Test
    void unknownRoute_returnsNotFound() throws Exception {
        mockMvc.perform(get("/does-not-exist"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Not found"));
    }
}
