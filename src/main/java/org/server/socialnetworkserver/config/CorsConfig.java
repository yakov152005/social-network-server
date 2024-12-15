package org.server.socialnetworkserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean(name = "customCorsConfig")
    public WebMvcConfigurer corsConfig(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // מאפשר לכל ה-Endpoints
                        .allowedOrigins("http://localhost:3000") // מקור ה-CORS שמותר
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // שיטות HTTP שמותרות
                        .allowedHeaders("*") // כל הכותרות שמותרות
                        .allowCredentials(true); // אם צריך Cookies או Authorization Headers
            }
        };
    }
}
