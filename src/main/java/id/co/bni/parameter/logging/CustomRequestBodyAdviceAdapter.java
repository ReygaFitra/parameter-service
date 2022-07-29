package id.co.bni.parameter.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Type;
import java.util.Optional;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class CustomRequestBodyAdviceAdapter extends RequestBodyAdviceAdapter {

    private final LoggingService loggingService;

    private final HttpServletRequest httpServletRequest;

    @Override
    public boolean supports(@NotNull MethodParameter methodParameter, @NotNull Type type, @NotNull Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @NotNull
    @Override
    public Object afterBodyRead(@NotNull Object body, @NotNull HttpInputMessage inputMessage, @NotNull MethodParameter parameter, @NotNull Type targetType, @NotNull Class<? extends HttpMessageConverter<?>> converterType) {
        String str = "";
        try {
            str = new ObjectMapper().writeValueAsString(body);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        final String bodyStr = str;

        Optional.of(body).ifPresentOrElse(value ->
                        loggingService.logRequest(httpServletRequest, bodyStr),
                () -> loggingService.logRequest(httpServletRequest, body));

        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }

}
