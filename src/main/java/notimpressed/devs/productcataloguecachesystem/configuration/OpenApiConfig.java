package notimpressed.devs.productcataloguecachesystem.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("Product Catalog API")
                        .description("REST API for managing products with caching")
                        .version("v0.0.1")
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT")));

    }
}
