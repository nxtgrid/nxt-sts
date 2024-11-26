package co.nxtgrid.tokens.annotation;

import co.nxtgrid.tokens.annotation.exception.InvalidTemplateException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class NotificationUtils {

    public String template(String str,
                           Map<String, Object> functionParams)
            throws InvalidTemplateException {
        Pattern pattern = Pattern.compile("\\{.+?\\}");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String template = matcher.group(0)
                    .replaceAll("\\{", "")
                    .replaceAll("\\}", "");
            if (functionParams.containsKey(template)) {
                str = str.replaceAll(String.format("\\{%s\\}", template),
                        functionParams.get(template).toString());
                continue;
            }
            throw new InvalidTemplateException(String
                    .format("Key %s not available as function param",
                            template));

        }
        return str;
    }
}
