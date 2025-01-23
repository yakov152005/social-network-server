package org.server.socialnetworkserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentConfig {
    private final Environment env;

    @Autowired
    public EnvironmentConfig(Environment env) {
        this.env = env;
    }

    public String getProperty(String key) {
        return env.getProperty(key);
    }
}
