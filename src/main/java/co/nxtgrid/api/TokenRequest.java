package co.nxtgrid.api;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

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

    @Schema(
        description = "STS token type to generate. Prefer TOP_UP_KWH; TOP_UP is a deprecated "
            + "alias for the same electricity kWh credit token.",
        example = "TOP_UP_KWH",
        allowableValues = { "TOP_UP_KWH", "TOP_UP", "CLEAR_CREDIT", "CLEAR_TAMPER", "SET_POWER_LIMIT" }
    )
    @NotNull
    private TokenType type;

    @Schema(
        description = "Token issue date/time in ISO 8601 format. Optional fractional seconds and "
            + "UTC/offset suffixes are accepted; any offset is ignored and the wall-clock date "
            + "and time fields are interpreted as UTC for token generation.",
        example = "2024-03-15T10:30:00",
        type = "string",
        format = "date-time"
    )
    @NotNull(message = "issueDate is required")
    @JsonDeserialize(using = IssueDateDeserializer.class)
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
        description = "Amount of electricity credit in kWh. Required when type is TOP_UP_KWH "
            + "(or deprecated alias TOP_UP). Must be zero or greater and at most 1820162.4 "
            + "(STS 16-bit amount maximum). Encoded in 0.1 kWh steps: values below 1 kWh "
            + "are ceiled to the next tenth; values at or above 1 kWh are truncated to a "
            + "tenth. Prefer multiples of 0.1. See README.",
        example = "0.5",
        minimum = "0",
        maximum = "1820162.4"
    )
    @PositiveOrZero(message = "kwh must be zero or greater")
    @DecimalMax(value = "1820162.4", message = "kwh must not exceed 1820162.4")
    private Double kwh;

    @Schema(
        description = "Maximum power limit value. Required when type is SET_POWER_LIMIT. Must be zero or greater.",
        example = "5000",
        minimum = "0"
    )
    @PositiveOrZero(message = "powerLimit must be zero or greater")
    private Long powerLimit;

    @AssertTrue(message = "kwh is required for TOP_UP_KWH")
    @JsonIgnore
    public boolean isKwhValidForType() {
        return type != TokenType.TOP_UP_KWH || kwh != null;
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
