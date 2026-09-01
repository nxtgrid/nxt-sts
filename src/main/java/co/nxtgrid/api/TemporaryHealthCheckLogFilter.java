package co.nxtgrid.api;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * TEMPORARY: log each App Platform / Docker health probe. Remove after wiring is confirmed.
 */
@Component
public class TemporaryHealthCheckLogFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(TemporaryHealthCheckLogFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        if ("GET".equals(request.getMethod())
                && "/actuator/health".equals(request.getRequestURI())) {
            log.info("[TEMPORARY] health check");
        }
        filterChain.doFilter(request, response);
    }
}
