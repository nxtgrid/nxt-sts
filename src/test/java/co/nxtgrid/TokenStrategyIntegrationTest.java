package co.nxtgrid;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TokenStrategyIntegrationTest {

    private static final String DECODER_KEY = "0123456789ABCDEF";
    private static final String ISSUE_DATE = "2024-03-15T10:30:00";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void topUp_producesExpectedToken() throws Exception {
        postToken(
            """
            {
              "type": "TOP_UP",
              "issueDate": "%s",
              "randomNumber": 3,
              "decoderKey": "%s",
              "kwh": 50.0
            }
            """.formatted(ISSUE_DATE, DECODER_KEY)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("41721561956736894278"));
    }

    @Test
    void clearCredit_producesExpectedToken() throws Exception {
        postToken(
            """
            {
              "type": "CLEAR_CREDIT",
              "issueDate": "%s",
              "randomNumber": 3,
              "decoderKey": "%s"
            }
            """.formatted(ISSUE_DATE, DECODER_KEY)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("26950712002385255186"));
    }

    @Test
    void clearTamper_producesExpectedToken() throws Exception {
        postToken(
            """
            {
              "type": "CLEAR_TAMPER",
              "issueDate": "%s",
              "randomNumber": 3,
              "decoderKey": "%s"
            }
            """.formatted(ISSUE_DATE, DECODER_KEY)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("15281476956190272101"));
    }

    @Test
    void setPowerLimit_producesExpectedToken() throws Exception {
        postToken(
            """
            {
              "type": "SET_POWER_LIMIT",
              "issueDate": "%s",
              "randomNumber": 3,
              "decoderKey": "%s",
              "powerLimit": 5000
            }
            """.formatted(ISSUE_DATE, DECODER_KEY)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("28227747371740445170"));
    }

    private org.springframework.test.web.servlet.ResultActions postToken(String body) throws Exception {
        return mockMvc.perform(
            post("/token").contentType(MediaType.APPLICATION_JSON).content(body)
        );
    }
}
