package co.nxtgrid.api;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Successful STS token generation response")
public class TokenResponse {

    @Schema(
        description = "20-digit IEC 62055-41 STS prepayment token",
        example = "58627975513348563046",
        pattern = "^\\d{20}$"
    )
    private String token;

    public TokenResponse() {
    }

    public TokenResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
