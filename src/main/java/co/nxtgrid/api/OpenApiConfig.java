package co.nxtgrid.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI(@Value("${info.app.version}") String version) {
        return new OpenAPI()
            .info(
                new Info()
                    .title("NXT STS")
                    .version(version)
                    .description("IEC 62055-41 STS prepayment token generation service")
            );
    }
}
