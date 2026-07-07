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
class TokenControllerValidationTest {

    private static final String VALID_TOP_UP = """
        {
          "type": "TOP_UP",
          "issueDate": "2024-03-15T10:30:00",
          "randomNumber": 3,
          "decoderKey": "0123456789ABCDEF",
          "kwh": 0.5
        }
        """;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void rejectsDecoderKeyWithWrongLength() throws Exception {
        mockMvc.perform(
            post("/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "type": "TOP_UP",
                      "issueDate": "2024-03-15T10:30:00",
                      "randomNumber": 3,
                      "decoderKey": "0123456789ABCDE",
                      "kwh": 0.5
                    }
                    """
                )
        )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("decoderKey must be exactly 16 hex characters"))
            .andExpect(jsonPath("$.field").value("decoderKey"));
    }

    @Test
    void rejectsUnknownTokenType() throws Exception {
        mockMvc.perform(
            post("/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "type": "INVALID",
                      "issueDate": "2024-03-15T10:30:00",
                      "randomNumber": 3,
                      "decoderKey": "0123456789ABCDEF",
                      "kwh": 0.5
                    }
                    """
                )
        )
            .andExpect(status().isBadRequest())
            .andExpect(
                jsonPath("$.error")
                    .value("type must be one of: TOP_UP, CLEAR_CREDIT, CLEAR_TAMPER, SET_POWER_LIMIT")
            );
    }

    @Test
    void rejectsRandomNumberAboveFifteen() throws Exception {
        mockMvc.perform(
            post("/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "type": "TOP_UP",
                      "issueDate": "2024-03-15T10:30:00",
                      "randomNumber": 16,
                      "decoderKey": "0123456789ABCDEF",
                      "kwh": 0.5
                    }
                    """
                )
        )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("randomNumber must be an integer between 0 and 15"))
            .andExpect(jsonPath("$.field").value("randomNumber"));
    }

    @Test
    void rejectsRandomNumberOverflow() throws Exception {
        mockMvc.perform(
            post("/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "type": "TOP_UP",
                      "issueDate": "2024-03-15T10:30:00",
                      "randomNumber": 125489697135,
                      "decoderKey": "0123456789ABCDEF",
                      "kwh": 0.5
                    }
                    """
                )
        )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("randomNumber must be an integer between 0 and 15"));
    }

    @Test
    void rejectsMissingRandomNumber() throws Exception {
        mockMvc.perform(
            post("/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "type": "TOP_UP",
                      "issueDate": "2024-03-15T10:30:00",
                      "decoderKey": "0123456789ABCDEF",
                      "kwh": 0.5
                    }
                    """
                )
        )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("randomNumber is required"))
            .andExpect(jsonPath("$.field").value("randomNumber"));
    }

    @Test
    void rejectsMissingKwhForTopUp() throws Exception {
        mockMvc.perform(
            post("/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "type": "TOP_UP",
                      "issueDate": "2024-03-15T10:30:00",
                      "randomNumber": 3,
                      "decoderKey": "0123456789ABCDEF"
                    }
                    """
                )
        )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("kwh is required for TOP_UP"));
    }

    @Test
    void rejectsMalformedIssueDate() throws Exception {
        mockMvc.perform(
            post("/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "type": "TOP_UP",
                      "issueDate": "not-a-date",
                      "randomNumber": 3,
                      "decoderKey": "0123456789ABCDEF",
                      "kwh": 0.5
                    }
                    """
                )
        )
            .andExpect(status().isBadRequest())
            .andExpect(
                jsonPath("$.error")
                    .value(
                        "issueDate must be an ISO 8601 datetime, e.g. \"2024-03-15T10:30:00\" or "
                            + "\"2026-07-07T10:12:54.289Z\" (time zone offset ignored)"
                    )
            );
    }

    @Test
    void acceptsValidRequest() throws Exception {
        mockMvc.perform(
            post("/token").contentType(MediaType.APPLICATION_JSON).content(VALID_TOP_UP)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").isString())
            .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void acceptsIssueDateWithFractionalSeconds() throws Exception {
        mockMvc.perform(
            post("/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "type": "TOP_UP",
                      "issueDate": "2026-07-07T10:12:54.289",
                      "randomNumber": 3,
                      "decoderKey": "0123456789ABCDEF",
                      "kwh": 0.5
                    }
                    """
                )
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").isString())
            .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void acceptsIssueDateWithUtcOffset() throws Exception {
        mockMvc.perform(
            post("/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "type": "TOP_UP",
                      "issueDate": "2026-07-07T10:12:54.289Z",
                      "randomNumber": 3,
                      "decoderKey": "0123456789ABCDEF",
                      "kwh": 0.5
                    }
                    """
                )
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").isString())
            .andExpect(jsonPath("$.token").isNotEmpty());
    }
}
