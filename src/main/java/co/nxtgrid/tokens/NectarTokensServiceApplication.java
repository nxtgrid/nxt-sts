package co.nxtgrid.tokens;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "co.nxtgrid.tokens")
@ConfigurationPropertiesScan("co.nxtgrid.tokens.configurations")
@EnableJpaRepositories(basePackages="co.nxtgrid.tokens.repository")
public class NectarTokensServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NectarTokensServiceApplication.class, args);
    }
}
