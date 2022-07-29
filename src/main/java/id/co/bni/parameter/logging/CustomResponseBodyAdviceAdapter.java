package id.co.bni.parameter.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class CustomResponseBodyAdviceAdapter implements ResponseBodyAdvice<Object> {

    private final LoggingService loggingService;

    @Override
    public boolean supports(@NotNull MethodParameter returnType, @NotNull Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, @NotNull MethodParameter returnType,
                                  @NotNull MediaType selectedContentType, @NotNull Class selectedConverterType,
                                  @NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response) {

        if (request instanceof ServletServerHttpRequest && response instanceof ServletServerHttpResponse) {
            String str = "";
            try {
                str = new ObjectMapper().writeValueAsString(body);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

            final String bodyStr = str;
            Optional.ofNullable(body).ifPresentOrElse(value ->
                            loggingService.logResponse(((ServletServerHttpRequest) request).getServletRequest(),
                                    ((ServletServerHttpResponse) response).getServletResponse(), bodyStr),
                    () -> loggingService.logResponse(((ServletServerHttpRequest) request).getServletRequest(),
                            ((ServletServerHttpResponse) response).getServletResponse(), body));
        }
        return body;
    }
}
