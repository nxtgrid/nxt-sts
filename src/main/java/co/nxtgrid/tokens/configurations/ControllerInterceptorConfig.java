package co.nxtgrid.tokens.configurations;

import co.nxtgrid.tokens.journal.interceptor.ControllerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ControllerInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    ControllerInterceptor controllerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(controllerInterceptor);
    }
}

