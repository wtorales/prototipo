package py.com.fi.prototipo.configurations;


import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.time.Duration;

/**
 * Clase de configuracion inicial o de arranque de aplicacion
 * @author wtorales
 * @fecha 29/11/2021
 */
@Configuration
public class PrototipoInitConfig {
    /**
     * Se establece el tiempo en que las conexiones a servicios externos quedan activas
     * @return RestTemplateBuilder().setConnectTimeOut
     */
    @Bean
    public RestTemplate getresttemplate() {
        return new RestTemplateBuilder().setConnectTimeout(Duration.ofMillis(300_000L))
                .setReadTimeout(Duration.ofMillis(300_000L)).build();
    }

    /**
     * Se establece como generico los recursos que pueden acceder al servicio
     * @return CorsRegistry.addCorsMappings
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").allowedMethods("*").allowedHeaders("*");
            }
        };
    }

}
