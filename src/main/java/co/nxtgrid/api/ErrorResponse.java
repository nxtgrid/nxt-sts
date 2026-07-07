package co.nxtgrid.api;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Structured error response")
public class ErrorResponse {

    @Schema(description = "Human-readable error message", example = "randomNumber is required")
    private String error;

    @Schema(
        description = "Request field associated with the error, when applicable",
        example = "randomNumber",
        nullable = true
    )
    private String field;

    public ErrorResponse() {
    }

    public ErrorResponse(String error, String field) {
        this.error = error;
        this.field = field;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
