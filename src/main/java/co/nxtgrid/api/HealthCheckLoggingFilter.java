package co.nxtgrid.api;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class HealthCheckLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(HealthCheckLoggingFilter.class);

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !"/actuator/health".equals(request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(request, response);
        log.info(
                "Health endpoint called: method={} path={} status={} userAgent={}",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                request.getHeader("User-Agent"));
    }
}
