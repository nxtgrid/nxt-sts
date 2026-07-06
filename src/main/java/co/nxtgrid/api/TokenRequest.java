package co.nxtgrid.api;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Request to generate a 20-digit IEC 62055-41 STS prepayment token")
public class TokenRequest {

    @Schema(
        description = "Meter decoder key as a hexadecimal string (16 hex characters = 8 bytes)",
        example = "0123456789ABCDEF",
        pattern = "^[0-9A-Fa-f]{16}$"
    )
    @NotNull
    @Pattern(regexp = "^[0-9A-Fa-f]{16}$", message = "decoderKey must be exactly 16 hex characters")
    private String decoderKey;

    @Schema(description = "STS token type to generate", example = "TOP_UP")
    @NotNull
    private TokenType type;

    @Schema(
        description = "Token issue date/time in ISO 8601 format",
        example = "2024-03-15T10:30:00",
        type = "string",
        format = "date-time"
    )
    @NotNull(message = "issueDate is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime issueDate;

    @Schema(
        description = "STS RND field (4 bits). Must be an integer from 0 to 15. "
            + "Vary between token issues to avoid duplicate-token rejection on the meter. "
            + "This is not a meter serial number or other large identifier.",
        minimum = "0",
        maximum = "15",
        example = "3"
    )
    @NotNull(message = "randomNumber is required")
    @Min(value = 0, message = "randomNumber must be an integer between 0 and 15")
    @Max(value = 15, message = "randomNumber must be an integer between 0 and 15")
    private Integer randomNumber;

    @Schema(
        description = "Amount of electricity credit in kWh. Required when type is TOP_UP.",
        example = "0.5"
    )
    private Double kwh;

    @Schema(
        description = "Maximum power limit value. Required when type is SET_POWER_LIMIT.",
        example = "5000"
    )
    private Long powerLimit;

    @AssertTrue(message = "kwh is required for TOP_UP")
    @JsonIgnore
    public boolean isKwhValidForType() {
        return type != TokenType.TOP_UP || kwh != null;
    }

    @AssertTrue(message = "powerLimit is required for SET_POWER_LIMIT")
    @JsonIgnore
    public boolean isPowerLimitValidForType() {
        return type != TokenType.SET_POWER_LIMIT || powerLimit != null;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public Integer getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(Integer randomNumber) {
        this.randomNumber = randomNumber;
    }

    public Double getKwh() {
        return kwh;
    }

    public void setKwh(Double kwh) {
        this.kwh = kwh;
    }

    public String getDecoderKey() {
        return decoderKey;
    }

    public void setDecoderKey(String decoderKey) {
        this.decoderKey = decoderKey;
    }

    public Long getPowerLimit() {
        return powerLimit;
    }

    public void setPowerLimit(Long powerLimit) {
        this.powerLimit = powerLimit;
    }
}
