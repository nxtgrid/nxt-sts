package co.nxtgrid.api;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.nxtgrid.strategy.TokenStrategy;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/token")
public class TokenController {

    private final List<TokenStrategy> strategies;

    public TokenController(List<TokenStrategy> strategies) {
        this.strategies = strategies;
    }

    @PostMapping
    TokenResponse generateToken(@Valid @RequestBody TokenRequest body) {
        try {
            TokenStrategy strategy = strategies.stream()
                .filter(s -> s.supports(body.getType()))
                .findFirst()
                .orElse(null);
            if (strategy == null) {
                return null;
            }
            return new TokenResponse(strategy.generate(body));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
