package co.nxtgrid.api;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.nxtgrid.strategy.TokenStrategy;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/token")
public class TokenController {

    private final List<TokenStrategy> strategies;

    public TokenController(List<TokenStrategy> strategies) {
        this.strategies = strategies;
    }

    @Operation(
        summary = "Generate STS token",
        description = "Generates a 20-digit IEC 62055-41 prepayment token using the Standard Transfer Algorithm (STA / EA07)."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Token generated successfully",
            content = @Content(schema = @Schema(implementation = TokenResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request (validation, unknown type, or malformed JSON)",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error during token generation",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping
    TokenResponse generateToken(@Valid @RequestBody TokenRequest body) throws Exception {
        TokenStrategy strategy = strategies.stream()
            .filter(s -> s.supports(body.getType()))
            .findFirst()
            .orElseThrow(() -> new UnsupportedTokenTypeException(body.getType()));
        return new TokenResponse(strategy.generate(body));
    }
}
