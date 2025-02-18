package com.tnc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
@Data
public class ModelConfig {
    private String modelPath;
    private int maxTokens = 2048;
    private float temperature = 0.7f;
    private String modelName = "llama";
}
