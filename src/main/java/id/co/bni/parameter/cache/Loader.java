package id.co.bni.parameter.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class Loader {

    private final ParameterLoader parameterLoader;

    @Bean
    public void load() {
        long start = System.currentTimeMillis();
        parameterLoader.load();
        log.info("Elapsed time = " + (System.currentTimeMillis() - start));
    }
}
