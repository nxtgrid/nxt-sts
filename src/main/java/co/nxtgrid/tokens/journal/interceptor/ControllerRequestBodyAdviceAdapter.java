package co.nxtgrid.tokens.journal.interceptor;

import co.nxtgrid.tokens.journal.JournalManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Type;

@ControllerAdvice
public class ControllerRequestBodyAdviceAdapter extends RequestBodyAdviceAdapter {

    @Autowired
    HttpServletRequest httpServletRequest;

    @Value("${journal.name}")
    private String pathToJournal;

    @Override
    public boolean supports(MethodParameter methodParameter, Type type,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage,
                                MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {

        try {
            JournalManager journalManager = new JournalManager(pathToJournal);
            journalManager.initialize();
            if (journalManager.open()) {
                journalManager.write(journalManager.serialize(httpServletRequest, body), "REQUEST");
                journalManager.close();
            }
        } catch (IOException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }

        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }

}
