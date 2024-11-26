package co.nxtgrid.tokens.journal.interceptor;

import co.nxtgrid.tokens.journal.JournalManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.IOException;

@ControllerAdvice
public class ControllerResponseBodyAdviceAdapter implements ResponseBodyAdvice<Object> {

    @Value("${journal.name}")
    private String pathToJournal;

    @Override
    public boolean supports(MethodParameter methodParameter,
                            Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o,
                                  MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {

        if (serverHttpRequest instanceof ServletServerHttpRequest &&
                serverHttpResponse instanceof ServletServerHttpResponse) {
            try {
                JournalManager journalManager = new JournalManager(pathToJournal);
                journalManager.initialize();
                if (journalManager.open()) {
                    journalManager.write(journalManager.serialize(o), "RESPONSE");
                    journalManager.close();
                }
            } catch (IOException | IllegalAccessException e) {
                System.out.println(e.getMessage());
            }
        }

        return o;
    }
}
