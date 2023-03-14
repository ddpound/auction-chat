package com.example.auctionchat.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.connection.ConnectionPoolSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class MongoConfig {

    @Bean
    public MongoClientSettings mongoClientSettings() {

        return MongoClientSettings.builder()
                .retryWrites(true)
                .applyToConnectionPoolSettings((ConnectionPoolSettings.Builder builder) -> {
                    builder.maxSize(500) //connections count
                            .minSize(5)
                            .maxConnectionLifeTime(5, TimeUnit.SECONDS)
                            .maxConnectionIdleTime(5, TimeUnit.SECONDS)
                            .maxWaitTime(5000, TimeUnit.SECONDS);
                })
                .applyToSocketSettings(builder -> {
                    builder.connectTimeout(2000, TimeUnit.MILLISECONDS);
                })
                .applicationName("app")
                .build();
    }

}
