package notimpressed.devs.productcataloguecachesystem.configuration;

import org.zalando.logbook.HttpRequest;
import org.zalando.logbook.Logbook;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.core.CurlHttpLogFormatter;
import org.zalando.logbook.core.DefaultHttpLogWriter;
import org.zalando.logbook.core.DefaultSink;
import java.util.function.Predicate;

@Configuration
public class LogbookConfig {

    @Bean
    public Logbook logbook() {
        Predicate<HttpRequest> isSwagger = request -> {
            String path = request.getPath();
            return path.startsWith("/swagger-ui")
                    || path.startsWith("/v3/api-docs")
                    || path.startsWith("/v3/swagger-config");
        };

        return Logbook.builder()
                .condition(isSwagger.negate())
                .sink(new DefaultSink(
                        new CurlHttpLogFormatter(),
                        new DefaultHttpLogWriter()
                ))
                .build();
    }
}
