package notimpressed.devs.productcataloguecachesystem.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.cache.CaffeineCacheMetrics;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(MeterRegistry registry) {
        CaffeineCacheManager manager = new CaffeineCacheManager("product", "productsByCategory");
        manager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .recordStats());

        manager.getCacheNames().forEach(name -> {
            CaffeineCache cache = (CaffeineCache) manager.getCache(name);
            if (cache != null) {
                CaffeineCacheMetrics.monitor(registry, cache.getNativeCache(), name);
            }
        });

        return manager;
    }
}
