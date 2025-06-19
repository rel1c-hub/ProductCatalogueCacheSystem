package notimpressed.devs.productcataloguecachesystem.configuration;

import org.zalando.logbook.Logbook;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.core.CurlHttpLogFormatter;
import org.zalando.logbook.core.DefaultHttpLogWriter;
import org.zalando.logbook.core.DefaultSink;


@Configuration
public class LogbookConfig {

    @Bean
    public Logbook logbook() {
        return Logbook.builder()
                .sink(new DefaultSink(
                        new CurlHttpLogFormatter(), new DefaultHttpLogWriter()
                ))
                .build();
    }
}
