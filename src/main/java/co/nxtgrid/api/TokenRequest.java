package co.nxtgrid.api;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class TokenRequest {

    @NotNull
    @Pattern(regexp = "^[0-9A-Fa-f]{16}$", message = "decoderKey must be exactly 16 hex characters")
    private String decoderKey;

    @NotNull
    private TokenType type;

    @NotNull(message = "issueDate is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime issueDate;

    @NotNull(message = "randomNumber is required")
    @Min(value = 0, message = "randomNumber must be an integer between 0 and 15")
    @Max(value = 15, message = "randomNumber must be an integer between 0 and 15")
    private Integer randomNumber;

    private Double kwh;
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
