package org.server.socialnetworkserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentConfig {
    private static Environment env;

    @Autowired
    public void setEnvironment(Environment environment) {
        env = environment;
    }

    public static String getProperty(String key) {
        return env.getProperty(key);
    }
}
