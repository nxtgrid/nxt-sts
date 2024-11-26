package co.nxtgrid.tokens.journal.interceptor;

import co.nxtgrid.tokens.journal.JournalManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.DispatcherType;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ControllerInterceptor implements HandlerInterceptor {

    @Value("${journal.name}")
    private String pathToJournal;

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (DispatcherType.REQUEST.name().equals(request.getDispatcherType().name())
                && (request.getMethod().equals(HttpMethod.GET.name())
                || request.getMethod().equals(HttpMethod.PUT.name())
                || request.getMethod().equals(HttpMethod.DELETE.name()))) {
            JournalManager journalManager = new JournalManager(pathToJournal);
            journalManager.initialize();
            if (journalManager.open()) {
                journalManager.write(journalManager.serialize(request), "REQUEST");
                journalManager.close();
            }
        }
        return true;
    }

}
