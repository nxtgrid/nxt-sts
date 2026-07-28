package co.nxtgrid.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    private final String version;

    public RootController(@Value("${info.app.version}") String version) {
        this.version = version;
    }

    @GetMapping("/")
    public ServiceInfo index() {
        return new ServiceInfo(
            "nxt-sts",
            version,
            "IEC 62055-41 STS prepayment token generation service",
            Map.of(
                "token", "POST /token",
                "health", "GET /actuator/health",
                "openapi", "GET /v3/api-docs",
                "swaggerUi", "GET /swagger"
            )
        );
    }
}
