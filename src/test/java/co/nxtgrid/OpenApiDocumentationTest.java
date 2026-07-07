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
class OpenApiDocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void apiDocs_returnsOpenApiSpec() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.info.title").value("NXT STS"))
            .andExpect(jsonPath("$.paths['/token'].post").exists())
            .andExpect(jsonPath("$.components.schemas.TokenRequest.properties.randomNumber.minimum").value(0))
            .andExpect(jsonPath("$.components.schemas.TokenRequest.properties.randomNumber.maximum").value(15));
    }

    @Test
    void swaggerUi_isAvailable() throws Exception {
        mockMvc.perform(get("/swagger"))
            .andExpect(status().is3xxRedirection());
    }
}
