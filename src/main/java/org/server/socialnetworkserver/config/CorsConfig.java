package org.server.socialnetworkserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CorsConfig {
    @Bean(name = "customCorsConfig")
    public WebMvcConfigurer corsConfig(){
        System.out.println("CORS configuration is being initialized...");
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("https://social-network-client-k8fp.onrender.com")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
                System.out.println("CORS mapping added for origin: http://localhost:3000");
            }
        };
    }
}
